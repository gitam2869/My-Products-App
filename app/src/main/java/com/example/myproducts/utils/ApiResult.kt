package com.example.myproducts.utils

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null,
    val isLoading: Boolean = false
) {
    class Success<T>(data: T) : ApiResult<T>(data = data)
    class Error<T>(message: String) : ApiResult<T>(message = message)
    class Loading<T>(isLoading: Boolean) : ApiResult<T>(isLoading = isLoading)
}
