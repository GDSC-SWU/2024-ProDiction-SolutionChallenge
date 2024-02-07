package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestScoreDto(
    @SerialName("studyId") val studyId: Int,
    @SerialName("score") val score: Double
)
