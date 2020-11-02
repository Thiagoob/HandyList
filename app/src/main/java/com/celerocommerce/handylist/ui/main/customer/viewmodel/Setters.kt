package com.celerocommerce.handylist.ui.main.customer.viewmodel

import com.celerocommerce.handylist.models.Customer

fun CustomerViewModel.setDailyCustomers(dailyCustomers: List<Customer>) {
    val update = getCurrentViewStateOrNew()
    update.dailyCustomersFields.dailyCustomers = dailyCustomers
    setViewState(update)
}

fun CustomerViewModel.setCustomer(customer: Customer) {
    val update = getCurrentViewStateOrNew()
    if (update.viewCustomerFields.customer == customer) {
        return
    }
    update.viewCustomerFields.customer = customer
    setViewState(update)
}

fun CustomerViewModel.setIsHandled(isHandled: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.viewCustomerFields.customer?.let {
        if (update.viewCustomerFields.customer?.isHandled == isHandled) {
            return
        }
        it.isHandled = isHandled
        setViewState(update)
    }
}