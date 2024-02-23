package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Url

@Serializable
data class MyPageDto(
    @SerialName("memberId") val categmemberIdoryId: Int,
    @SerialName("nickname") val nickname: String,
    @SerialName("googleProfile") val googleProfile: String,
    @SerialName("stage") val stage: String?,
    @SerialName("age") val age: Int?
)
