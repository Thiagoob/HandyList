package com.celerocommerce.handylist.ui.main.customer.state

sealed class CustomerStateEvent {

    object GetDailyCustomersEvent: CustomerStateEvent()

    object SetCustomerHandledEvent: CustomerStateEvent()

    object None: CustomerStateEvent()
}