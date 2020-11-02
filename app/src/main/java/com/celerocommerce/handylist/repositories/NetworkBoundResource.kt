package com.celerocommerce.handylist.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.celerocommerce.handylist.network.responses.ApiResponse
import com.celerocommerce.handylist.ui.DataState
import com.celerocommerce.handylist.ui.Response
import com.celerocommerce.handylist.ui.ResponseType
import com.celerocommerce.handylist.util.Constants.NETWORK_TIMEOUT
import com.celerocommerce.handylist.util.Constants.TESTING_CACHE_DELAY
import com.celerocommerce.handylist.util.Constants.TESTING_NETWORK_DELAY
import com.celerocommerce.handylist.util.ErrorHandling
import com.celerocommerce.handylist.util.ErrorHandling.ERROR_CHECK_NETWORK_CONNECTION
import com.celerocommerce.handylist.util.ErrorHandling.UNABLE_TODO_OPERATION_WO_INTERNET
import com.celerocommerce.handylist.util.ErrorHandling.UNABLE_TO_RESOLVE_HOST
import com.celerocommerce.handylist.util.ErrorHandling.UNKNOWN_ERROR
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import timber.log.Timber

@OptIn(InternalCoroutinesApi::class)
abstract class NetworkBoundResource<ResponseObject, CacheObject, ViewState>(
    isNetworkAvailable: Boolean, // is there a network connection?
    isNetworkRequest: Boolean, // is this a network request
    shouldCancelIfNoInternet: Boolean, // should this job be cancelled if there is no network?
    shouldLoadFromCache: Boolean // should the cached data be loaded
) {

    protected val result = MediatorLiveData<DataState<ViewState>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {

        setJob(initNewJob())
        setValue(DataState.loading(isLoading = true, null))

        if (shouldLoadFromCache) {
            val dbSource = loadFromCache()
            result.addSource(dbSource) {
                result.removeSource(dbSource)
                setValue(DataState.loading(isLoading = true, cachedData = it))
            }
        }

        if (isNetworkRequest) {
            if (isNetworkAvailable) {
                doNetworkRequest()
            } else {
                if (shouldCancelIfNoInternet) {
                    onErrorReturn(
                        UNABLE_TODO_OPERATION_WO_INTERNET,
                        shouldUseDialog = true,
                        shouldUseToast = false
                    )
                } else {
                    doCacheRequest()
                }
            }
        } else {
            doCacheRequest()
        }
    }

    private fun doCacheRequest() {
        Timber.d("Retrieving from cache")
        coroutineScope.launch {

            // fake delay for testing cache
            delay(TESTING_CACHE_DELAY)

            // View data from cache ONLY and return
            createCacheRequestAndReturn()
        }
    }

    private fun doNetworkRequest() {
        Timber.d("Retrieving from network")
        coroutineScope.launch {
            // simulate a network delay for testing
            delay(TESTING_NETWORK_DELAY)

            withContext(Main) {
                // make network call
                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    result.removeSource(apiResponse)

                    coroutineScope.launch {
                        handleNetworkCall(response)
                    }
                }
            }
        }


        GlobalScope.launch(IO) {
            delay(NETWORK_TIMEOUT)

            if (!job.isCompleted) {
                Timber.e("JOB NETWORK TIMEOUT.")
                job.cancel(CancellationException((UNABLE_TO_RESOLVE_HOST)))
            }
        }
    }

    private suspend fun handleNetworkCall(response: ApiResponse<ResponseObject>?) {
        when (response) {
            is ApiResponse.ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }

            is ApiResponse.ApiErrorResponse -> {
                Timber.e(response.errorMessage)
                onErrorReturn(response.errorMessage, shouldUseDialog = true, shouldUseToast = false)
            }

            is ApiResponse.ApiEmptyResponse -> {
                Timber.e("Request returned nothing (HTTP 204)")
                onErrorReturn(UNKNOWN_ERROR, shouldUseDialog = true, shouldUseToast = false)
            }
        }
    }

    fun onCompleteJob(dataState: DataState<ViewState>) {
        GlobalScope.launch(Main) {
            job.complete()
            setValue(dataState)
        }
    }

    private fun setValue(dataState: DataState<ViewState>) {
        result.value = dataState
    }

    protected fun onErrorReturn(
        errorMessage: String?,
        shouldUseDialog: Boolean,
        shouldUseToast: Boolean
    ) {
        var msg = errorMessage
        var useDialog = shouldUseDialog
        var responseType: ResponseType = ResponseType.None

        if (msg == null) {
            msg = UNKNOWN_ERROR
        } else if (ErrorHandling.isNetworkError(msg)) {
            msg = ERROR_CHECK_NETWORK_CONNECTION
            useDialog = false
        }

        if (shouldUseToast) {
            responseType = ResponseType.Toast
        }

        if (useDialog) {
            responseType = ResponseType.Dialog
        }

        onCompleteJob(
            DataState.error(
                response = Response(
                    message = msg,
                    responseType = responseType
                )
            )
        )
    }

    private fun initNewJob(): Job {
        Timber.d("called")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true) {
            if (job.isCancelled) {
                Timber.e("Job has been cancelled.")
                it?.let {
                    onErrorReturn(it.message, shouldUseDialog = false, shouldUseToast = true)
                } ?: onErrorReturn(UNKNOWN_ERROR, shouldUseDialog = false, shouldUseToast = true)
            } else if (job.isCompleted) {
                Timber.d("Job has been completed.")
                // Do nothing. Should be handled already
            }
        }

        coroutineScope = CoroutineScope(IO + job)

        return job
    }

    abstract suspend fun createCacheRequestAndReturn()

    abstract suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<ApiResponse<ResponseObject>>

    abstract fun loadFromCache(): LiveData<ViewState>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    abstract fun setJob(job: Job)

    fun asLiveData() = result as LiveData<DataState<ViewState>>
}