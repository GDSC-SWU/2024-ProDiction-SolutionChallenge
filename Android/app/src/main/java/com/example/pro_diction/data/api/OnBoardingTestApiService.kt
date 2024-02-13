package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.TestScoreDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface OnBoardingTestApiService {
    @Multipart
    @POST("/api/v1/test/diction")
    fun getScore(
        @Query("id") id: Int,
        @Part("multipartFile") multipartFile : RequestBody
    ): Call<BaseResponse<TestScoreDto>>
}