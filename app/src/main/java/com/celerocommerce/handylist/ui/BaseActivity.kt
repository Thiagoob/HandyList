package com.celerocommerce.handylist.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.celerocommerce.handylist.ui.ResponseType.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), DataStateChangeListener {

    override fun onDataStateChange(dataState: DataState<*>?) {

        dataState?.let {
            GlobalScope.launch(Main) {

                displayProgressBar(it.loading.isLoading)

                it.error?.let { errorEvent ->
                    handleStateError(errorEvent)
                }

                it.data?.let {
                    it.response?.let { responseEvent ->
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {

            when (it.response.responseType) {

                is Toast -> {
                    it.response.message?.let {
                        displayToast(it)
                    }
                }

                is Dialog -> {
                    it.response.message?.let {
                        displayErrorDialog(it)
                    }
                }

                is None -> {
                    Timber.e(it.response.message)
                }
            }

        }
    }

    private fun handleStateResponse(event: Event<Response>) {

        event.getContentIfNotHandled()?.let {

            when (it.responseType) {

                is Toast -> {
                    it.message?.let {
                        displayToast(it)
                    }
                }

                is Dialog -> {
                    it.message?.let {
                        displaySuccessDialog(it)
                    }
                }

                is None -> {
                    Timber.e(it.message)
                }
            }

        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    abstract fun displayProgressBar(bool: Boolean)

}
