package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudyResponseDto(
    @SerialName("studyId") val studyId: Int,
    @SerialName("content") val content: String
)
