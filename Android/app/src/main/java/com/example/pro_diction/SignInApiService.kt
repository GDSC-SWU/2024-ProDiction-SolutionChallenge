package com.example.pro_diction

import retrofit2.http.Header
import retrofit2.http.POST

interface SignInApiService {
    @POST("/api/v1/member/login/oauth/google")
    suspend fun postSignIn(
        @Header("id-token") idToken : String
    ): BaseResponse<ResponseSignInDto>
}