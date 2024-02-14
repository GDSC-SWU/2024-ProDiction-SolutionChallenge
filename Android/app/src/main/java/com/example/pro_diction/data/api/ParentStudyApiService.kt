package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.StudyResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ParentStudyApiService {
    @GET("/api/v1/study")
    fun getParentStudyId(
        @Query("parentStudyId") parentStudyId: Int
    ): Call<BaseResponse<List<StudyResponseDto>>>
}