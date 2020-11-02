package com.celerocommerce.handylist.network.responses

import retrofit2.Response
import timber.log.Timber


///**
// * Generic class for handling responses from Retrofit
// * @param <T>
//</T> */
//open class ApiResponse<T> {
//    fun create(error: Throwable): ApiResponse<T> {
//        return ApiErrorResponse<T>(if (error.message == "") error.message else "Unknown error\nCheck network connection")
//    }
//
//    fun create(response: Response<T>): ApiResponse<T> {
//        return if (response.isSuccessful) {
//            val body = response.body()
//            if (body == null || response.code() == 204) { // 204 is empty response
//                ApiEmptyResponse<T>()
//            } else {
//                ApiSuccessResponse<T>(body)
//            }
//        } else {
//            var errorMsg: String? = ""
//            errorMsg = try {
//                response.errorBody()!!.string()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                response.message()
//            }
//            ApiErrorResponse<T>(errorMsg)
//        }
//    }
//
//    /**
//     * Generic success response from api
//     * @param <T>
//    </T> */
//    class ApiSuccessResponse<T> internal constructor(val body: T) : ApiResponse<T>()
//
//    /**
//     * Generic Error response from API
//     * @param <T>
//    </T> */
//    class ApiErrorResponse<T> internal constructor(val errorMessage: String?) :
//        ApiResponse<T>()
//
//    /**
//     * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
//     */
//    class ApiEmptyResponse<T> : ApiResponse<T>()
//}

/**
 * Generic class for handling responses from Retrofit
 * @param <T>
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {

    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    class ApiEmptyResponse<T> : ApiResponse<T>()

    data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

    data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {

            Timber.d("response: ${response}")
            Timber.d("raw: ${response.raw()}")
            Timber.d("headers: ${response.headers()}")
            Timber.d("message: ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    return ApiEmptyResponse()
                } else if (response.code() == 401) {
                    return ApiErrorResponse("401 Unauthorized. Token may be invalid.")
                } else {
                    return ApiSuccessResponse(body = body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                return ApiErrorResponse(errorMsg ?: "unknown error")
            }
        }
    }
}