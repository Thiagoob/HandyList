package com.celerocommerce.handylist.ui.main.customer.state

import com.celerocommerce.handylist.models.Customer

data class CustomerViewState(

    // DailyCustomersFragment vars
    var dailyCustomersFields: DailyCustomersFields = DailyCustomersFields(),

    // ViewCustomerFragment vars
    var viewCustomerFields: ViewCustomerFields = ViewCustomerFields()

) {

    data class DailyCustomersFields(
        var dailyCustomers: List<Customer> = ArrayList()
    )

    data class ViewCustomerFields(
        var customer: Customer? = null
    )
}