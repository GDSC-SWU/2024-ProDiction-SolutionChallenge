package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.data.dto.StudyResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchApiService {
    @POST("/api/v1/search")
    fun postSearch(
        @Query("keyword") keyword : String
    ): Call<BaseResponse<List<StudyResponseDto>>>
}