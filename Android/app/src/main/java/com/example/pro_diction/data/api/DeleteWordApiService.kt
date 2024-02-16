package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.WordApiDto
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface DeleteWordApiService {
    @DELETE("/api/v1/vocabulary/{vocabularyId}")
    fun deleteWord(
        @Path("vocabularyId") vocabularyId: Int
    ): Call<BaseResponse<WordApiDto>>
}