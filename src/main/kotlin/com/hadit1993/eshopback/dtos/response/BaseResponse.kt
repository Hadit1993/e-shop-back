package com.hadit1993.eshopback.dtos.response

data class BaseResponse<T>(
    val data: T? = null,
    val success: Boolean,
    val message: String = "The operation was successful"
)
