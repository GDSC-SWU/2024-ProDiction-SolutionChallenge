package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface OnBoardingStageApiService {
    @POST("/api/v1/test")
    fun postStage(
        @Query("stage") stage : Int
    ): Call<Int>
}