package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.StudyResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SubCategoryApiService {
    @GET("/api/v1/study")
    fun getSubCategory(
        @Query("subCategoryId") subCategoryId: Int
    ): Call<BaseResponse<List<StudyResponseDto>>>

}