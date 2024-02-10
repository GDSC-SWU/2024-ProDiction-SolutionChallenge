package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RandomTestDto(
    @SerialName("content") val content: String,
    @SerialName("studyId") val studyId: Int
)