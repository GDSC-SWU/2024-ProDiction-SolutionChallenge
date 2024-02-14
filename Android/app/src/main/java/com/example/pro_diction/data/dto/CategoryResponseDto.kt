package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("studyResponseDtoList") val studyResponseDtoList: List<StudyResponseDto>
)
