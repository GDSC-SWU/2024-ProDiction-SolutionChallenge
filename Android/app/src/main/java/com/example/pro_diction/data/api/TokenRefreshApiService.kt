package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.ResponseSignInDto
import retrofit2.http.GET
import retrofit2.http.Header

interface TokenRefreshApiService {
    @GET("/api/v1/member/refresh")
    suspend fun getTokenRefresh(
    ): BaseResponse<ResponseSignInDto>
}