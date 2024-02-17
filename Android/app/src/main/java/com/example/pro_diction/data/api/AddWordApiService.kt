package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.WordApiDto
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface AddWordApiService {
    @POST("/api/v1/vocabulary/{studyId}")
    fun postWord(
        @Path("studyId") studyId: Int
    ): Call<BaseResponse<WordApiDto>>
}