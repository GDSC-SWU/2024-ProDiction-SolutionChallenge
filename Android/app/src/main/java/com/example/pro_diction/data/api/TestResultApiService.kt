package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.TestResultDto
import com.example.pro_diction.data.dto.TestScoreDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TestResultApiService {
    @Multipart
    @POST("/api/v1/study/{studyId}")
    fun postResult(
        @Path("studyId") id: Int,
        @Part multipartFile : MultipartBody.Part
    ): Call<BaseResponse<TestResultDto>>
}