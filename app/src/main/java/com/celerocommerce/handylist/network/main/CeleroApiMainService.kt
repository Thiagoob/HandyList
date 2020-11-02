package com.celerocommerce.handylist.network.main

import androidx.lifecycle.LiveData
import com.celerocommerce.handylist.network.responses.ApiResponse
import com.celerocommerce.handylist.network.responses.CustomerNetworkEntity
import retrofit2.http.GET

interface CeleroApiMainService {

    @GET("celerocustomers.json")
    fun getDailyCustomers(): LiveData<ApiResponse<List<CustomerNetworkEntity>>>
}