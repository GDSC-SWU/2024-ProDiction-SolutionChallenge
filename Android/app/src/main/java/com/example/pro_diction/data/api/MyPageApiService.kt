package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.MyPageDto
import retrofit2.Call
import retrofit2.http.GET

interface MyPageApiService {
    @GET("/api/v1/member")
    fun getMypage() : Call<BaseResponse<MyPageDto>>
}