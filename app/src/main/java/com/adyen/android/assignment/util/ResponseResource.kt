package com.adyen.android.assignment.util


sealed class ResponseResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResponseResource<T>()
    object Loading : ResponseResource<Nothing>()
    data class Error(val exception: String?) : ResponseResource<Nothing>()
}
