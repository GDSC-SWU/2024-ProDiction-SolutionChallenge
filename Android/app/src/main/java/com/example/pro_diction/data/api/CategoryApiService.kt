package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryDto
import com.example.pro_diction.data.dto.CategoryResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryApiService {
    @GET("/api/v1/study/subcategory")
    fun getCategory(
        @Query("categoryId") categoryId: Int,
        @Query("isFinalConsonant") isFinalConsonant: Boolean,
        @Query("studyCount") studyCount: Int
    ): Call<BaseResponse<List<CategoryResponseDto>>>
}