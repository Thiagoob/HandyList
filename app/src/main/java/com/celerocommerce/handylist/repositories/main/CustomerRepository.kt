package com.celerocommerce.handylist.repositories.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.network.main.CeleroApiMainService
import com.celerocommerce.handylist.network.main.CustomerNetworkMapper
import com.celerocommerce.handylist.network.responses.ApiResponse
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity
import com.celerocommerce.handylist.persistence.main.CustomerDao
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceEntity
import com.celerocommerce.handylist.persistence.main.CustomerPersistenceMapper
import com.celerocommerce.handylist.repositories.BaseRepository
import com.celerocommerce.handylist.repositories.NetworkBoundResource
import com.celerocommerce.handylist.session.SessionManager
import com.celerocommerce.handylist.ui.DataState
import com.celerocommerce.handylist.ui.main.customer.state.CustomerViewState
import com.celerocommerce.handylist.ui.main.customer.state.CustomerViewState.DailyCustomersFields
import com.celerocommerce.handylist.util.AbsentLiveData
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
data class CustomerRepository
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val celeroApiMainService: CeleroApiMainService,
    private val networkMapper: CustomerNetworkMapper,
    private val customerDao: CustomerDao,
    private val persistenceMapper: CustomerPersistenceMapper
) : BaseRepository(className = "CustomerRepository") {

    fun getDailyCustomers(): LiveData<DataState<CustomerViewState>> {
        return object :
            NetworkBoundResource<List<CustomerNetworkEntity>, List<CustomerPersistenceEntity>, CustomerViewState>(
                isNetworkAvailable = sessionManager.isNetworkAvailable(),
                isNetworkRequest = true,
                shouldCancelIfNoInternet = false,
                shouldLoadFromCache = true
            ) {

            private val idListCustomersOnCacheToBeDeleted = ArrayList<Long>()

            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {
                    // finish by viewing the db cache
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(
                                data = viewState,
                                response = null
                            )
                        )
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<List<CustomerNetworkEntity>>) {

                updateLocalDb(networkMapper.mapFromEntityList(response.body))

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<ApiResponse<List<CustomerNetworkEntity>>> {
                return celeroApiMainService.getDailyCustomers()
            }

            override fun loadFromCache(): LiveData<CustomerViewState> {
                return customerDao.getAllCustomers()
                    .switchMap {

                        idListCustomersOnCacheToBeDeleted.clear()
                        for (customerEntity in it) {
                            idListCustomersOnCacheToBeDeleted.add(customerEntity.id)
                        }

                        object : LiveData<CustomerViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = CustomerViewState(
                                    DailyCustomersFields(
                                        dailyCustomers = persistenceMapper.mapFromEntityList(it)
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<CustomerPersistenceEntity>?) {
                cacheObject?.let { customerFromNetworkList: List<CustomerPersistenceEntity> ->
                    withContext(IO) {

                        var idCustomerFromNetwork: Long
                        for (customerFromNetwork in customerFromNetworkList) {
                            idCustomerFromNetwork = customerFromNetwork.id

                            try {
                                launch {
                                    Timber.d("inserting or updating cache on customer: ${customerFromNetwork.name}")
                                    customerDao.upsert(customerFromNetwork)
                                }
                            } catch (e: Exception) {
                                Timber.e("error inserting or updating cache on customer: ${customerFromNetwork.name}")
                            }

                            if (idListCustomersOnCacheToBeDeleted.contains(idCustomerFromNetwork)) {
                                idListCustomersOnCacheToBeDeleted.remove(idCustomerFromNetwork)
                            }
                        }

                        for (idCustomerToBeDeleted in idListCustomersOnCacheToBeDeleted) {
                            try {
                                launch {
                                    Timber.d("deleting cache on customer with ID: $idCustomerToBeDeleted")
                                    customerDao.deleteById(idCustomerToBeDeleted)
                                }
                            } catch (e: Exception) {
                                Timber.d("error deleting cache on customer with ID: $idCustomerToBeDeleted")
                            }
                        }
                    }
                }
            }

            override fun setJob(job: Job) {
                addJob("getDailyCustomers", job)
            }

        }.asLiveData()
    }

    fun updateCustomer(customer: Customer): LiveData<DataState<CustomerViewState>> {
        return object :
            NetworkBoundResource<Nothing, List<CustomerPersistenceEntity>, CustomerViewState>(
                isNetworkAvailable = false,
                isNetworkRequest = false,
                shouldCancelIfNoInternet = false,
                shouldLoadFromCache = true
            ) {
            override suspend fun createCacheRequestAndReturn() {
                updateLocalDb(null)

                withContext(Main) {
                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(
                                data = viewState,
                                response = null
                            )
                        )
                    }
                }
            }

            // N/A
            override suspend fun handleApiSuccessResponse(response: ApiResponse.ApiSuccessResponse<Nothing>) {
            }

            // N/A
            override fun createCall(): LiveData<ApiResponse<Nothing>> {
                return AbsentLiveData.create()
            }

            override fun loadFromCache(): LiveData<CustomerViewState> {
                return customerDao.getAllCustomers()
                    .switchMap {
                        object : LiveData<CustomerViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = CustomerViewState(
                                    DailyCustomersFields(
                                        dailyCustomers = persistenceMapper.mapFromEntityList(it)
                                    )
                                )
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cacheObject: List<CustomerPersistenceEntity>?) {
                customerDao.update(persistenceMapper.mapToEntity(customer))
            }

            override fun setJob(job: Job) {
                addJob("updateCustomer", job)
            }

        }.asLiveData()
    }
}