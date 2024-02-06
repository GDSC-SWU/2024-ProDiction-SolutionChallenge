package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.PATCH
import retrofit2.http.Query

interface OnBoardingAgeApiService {
    @PATCH("/api/v1/member")
    fun patchAge(
        @Query("age") age: Int
    ): Call<Int>
}