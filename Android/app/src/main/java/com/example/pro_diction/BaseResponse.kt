package com.example.pro_diction

import kotlinx.serialization.SerialName

data class BaseResponse<T> (
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: T? = null
)