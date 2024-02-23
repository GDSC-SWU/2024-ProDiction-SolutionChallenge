package com.example.pro_diction.data.api

import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.SearchDeleteDto
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Path

interface SearchDeleteApiService {
    @DELETE("/api/v1/search/{search}")
    fun deleteSearch(
        @Path("search") search : Int
    ): Call<BaseResponse<SearchDeleteDto>>
}