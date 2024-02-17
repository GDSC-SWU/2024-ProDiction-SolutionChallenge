package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.WordApiDto
import com.example.pro_diction.data.dto.WordListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyWordApiService {
    @GET("/api/v1/vocabulary/{categoryId}")
    fun getWord(
        @Path("categoryId") categoryId: Int
    ): Call<BaseResponse<List<WordListDto>>>
}