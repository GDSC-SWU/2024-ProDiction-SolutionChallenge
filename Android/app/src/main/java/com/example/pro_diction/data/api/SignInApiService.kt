package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.ResponseSignInDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface SignInApiService {
    @POST("/api/v1/member/login/oauth/google")
    suspend fun postSignIn(
        @Header("id-token") idToken : String
    ): BaseResponse<ResponseSignInDto>
}
