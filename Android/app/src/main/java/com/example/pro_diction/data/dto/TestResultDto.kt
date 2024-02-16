package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestResultDto(
    @SerialName("studyId") val studyId: Int,
    @SerialName("score") val score: Float,
    @SerialName("sttResult") val sttResult: String,
    @SerialName("splitSttResult") val splitSttResult: String
)
