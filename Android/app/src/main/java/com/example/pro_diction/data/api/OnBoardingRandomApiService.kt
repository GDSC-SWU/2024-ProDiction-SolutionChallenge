package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.RandomTestDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OnBoardingRandomApiService {
    @GET("/api/v1/test/study")
    fun getRandomTest (
        @Query("stage") stage: Int
    ): Call<BaseResponse<List<RandomTestDto>>>

}