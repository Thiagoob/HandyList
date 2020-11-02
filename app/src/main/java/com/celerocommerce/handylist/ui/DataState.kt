package com.celerocommerce.handylist.ui

data class DataState<T>(
    var error: Event<StateError>? = null,
    var loading: Loading = Loading(false),
    var data: Data<T>? = null
) {

    companion object {

        fun <T> error(
            response: Response
        ): DataState<T> {
            return DataState(
                error = Event(
                    StateError(
                        response
                    )
                ),
                loading = Loading(false),
                data = null
            )
        }

        fun <T> loading(
            isLoading: Boolean,
            cachedData: T? = null
        ): DataState<T> {
            return DataState(
                error = null,
                loading = Loading(isLoading),
                data = Data(
                    Event.dataEvent(
                        cachedData
                    ),
                    null
                )
            )
        }

        fun <T> data(
            data: T? = null,
            response: Response? = null
        ): DataState<T> {
            return DataState(
                error = null,
                loading = Loading(false),
                data = Data(
                    Event.dataEvent(data),
                    Event.responseEvent(response)
                )
            )
        }
    }
}

//sealed class DataState<T>(
//    val data: Data<T>? = null,
//    val error: Event<StateError>? = null
//) {
//    class Success<T>(data: T?, response: Response? = null) :
//        DataState<T>(Data(Event.dataEvent(data), Event.responseEvent(response))) {
//
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as Success<*>
//
//            if (data != other.data) return false
//            if (error != other.error) return false
//
//            return true
//        }
//
//        override fun hashCode(): Int {
//            return super.hashCode()
//        }
//
//
//        override fun toString(): String {
//            return "Success(data=$data, error=$error)"
//        }
//
//    }
//
//    class Loading<T>(data: T? = null) : DataState<T>(Data(Event.dataEvent(data), null)) {
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as Loading<*>
//
//            if (data != other.data) return false
//            if (error != other.error) return false
//
//            return true
//        }
//
//        override fun hashCode(): Int {
//            return super.hashCode()
//        }
//
//        override fun toString(): String {
//            return "Loading(data=$data, error=$error)"
//        }
//    }
//
//    class Error<T>(response: Response) : DataState<T>(error = (Event(StateError(response)))) {
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//
//            other as Error<*>
//
//            if (data != other.data) return false
//            if (error != other.error) return false
//
//            return true
//        }
//
//        override fun hashCode(): Int {
//            return super.hashCode()
//        }
//
//        override fun toString(): String {
//            return "Error(data=$data, error=$error)"
//        }
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as DataState<*>
//
//        if (data != other.data) return false
//        if (error != other.error) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = data?.hashCode() ?: 0
//        result = 31 * result + (error?.hashCode() ?: 0)
//        return result
//    }
//
//
//}