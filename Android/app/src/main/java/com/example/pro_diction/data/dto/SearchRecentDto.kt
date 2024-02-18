package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRecentDto(
    @SerialName("searchId") val searchId: Int,
    @SerialName("searchContent") val searchContent: String,
    @SerialName("searchDate") val searchDate: String
)
