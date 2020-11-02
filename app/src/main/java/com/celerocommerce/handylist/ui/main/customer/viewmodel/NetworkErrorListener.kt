package com.celerocommerce.handylist.ui.main.customer.viewmodel

import com.celerocommerce.handylist.ui.Response

interface NetworkErrorListener {

    fun onNetworkError(response: Response)
}