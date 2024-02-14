package com.example.pro_diction.data.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("categoryId") val categoryId: Int,
    @SerialName("finalConsonant") val finalConsonant: Boolean,
    @SerialName("studyCount") val studyCount: Int
)
