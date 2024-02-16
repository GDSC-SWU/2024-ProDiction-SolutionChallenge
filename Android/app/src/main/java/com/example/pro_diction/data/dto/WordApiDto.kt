package com.example.pro_diction.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordApiDto(
    @SerialName("vocabularyId") val vocabularyId: Int,
    @SerialName("studyId") val studyId: Int,
    @SerialName("memberId") val memberId: Int
)
