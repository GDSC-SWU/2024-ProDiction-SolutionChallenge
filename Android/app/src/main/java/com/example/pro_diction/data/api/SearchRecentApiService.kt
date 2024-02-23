package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.data.dto.StudyResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchRecentApiService {
    @GET("/api/v1/search")
    fun getSearchRecent(): Call<BaseResponse<List<SearchRecentDto>>>
}