package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.StudyItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StudyIdApiService {
    @GET("/api/v1/study/{studyId}")
    fun getStudyId(
        @Path("studyId") studyId: Int
    ) : Call<BaseResponse<StudyItem>>
}