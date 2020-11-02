package com.celerocommerce.handylist.ui.main.customer.viewmodel

import com.celerocommerce.handylist.models.Customer

fun CustomerViewModel.getCustomer(): Customer {
    return getCurrentViewStateOrNew().viewCustomerFields.customer!!
}

fun CustomerViewModel.isCustomerHandled(): Boolean {
    return getCurrentViewStateOrNew().viewCustomerFields.customer?.isHandled ?: false
}