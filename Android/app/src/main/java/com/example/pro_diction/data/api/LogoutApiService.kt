package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.DELETE

interface LogoutApiService {
    @DELETE("/api/v1/member/logout/oauth/google")
    fun logout() : Call<String>
}