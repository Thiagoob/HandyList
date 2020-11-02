package com.celerocommerce.handylist.ui.main.customer.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.celerocommerce.handylist.models.Customer
import com.celerocommerce.handylist.repositories.main.CustomerRepository
import com.celerocommerce.handylist.session.SessionManager
import com.celerocommerce.handylist.ui.BaseViewModel
import com.celerocommerce.handylist.ui.DataState
import com.celerocommerce.handylist.ui.Response
import com.celerocommerce.handylist.ui.ResponseType
import com.celerocommerce.handylist.ui.main.customer.state.CustomerStateEvent
import com.celerocommerce.handylist.ui.main.customer.state.CustomerStateEvent.*
import com.celerocommerce.handylist.ui.main.customer.state.CustomerViewState
import com.celerocommerce.handylist.util.AbsentLiveData
import com.celerocommerce.handylist.util.ErrorHandling

class CustomerViewModel
@ViewModelInject
constructor(
    private val customerRepository: CustomerRepository,
    private val sessionManager: SessionManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<CustomerStateEvent, CustomerViewState>() {

    var networkErrorListener: NetworkErrorListener? = null

    override fun handleStateEvent(stateEvent: CustomerStateEvent): LiveData<DataState<CustomerViewState>> {

        return when (stateEvent) {

            is GetDailyCustomersEvent -> {
                if (!sessionManager.isNetworkAvailable()) {
                    networkErrorListener?.onNetworkError(
                        Response(
                            message = ErrorHandling.ERROR_CHECK_NETWORK_CONNECTION,
                            responseType = ResponseType.Toast
                        )
                    )
                }
                customerRepository.getDailyCustomers()
            }

            is SetCustomerHandledEvent -> {
                customerRepository.updateCustomer(getCustomer())
            }

            is None -> {
                AbsentLiveData.create()
            }
        }
    }

    override fun initViewState(): CustomerViewState {
        return CustomerViewState()
    }

    fun cancelActiveJobs() {
        customerRepository.cancelActiveJobs()
        handlePendingData()
    }

    private fun handlePendingData() {
        setStateEvent(None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    fun saveCustomer(key: String) {
        savedStateHandle.set(key, getCustomer())
    }

    fun restoreCustomer(key: String) {
        savedStateHandle.get<Customer>(key)?.let {
            setCustomer(it)
        }
    }
}