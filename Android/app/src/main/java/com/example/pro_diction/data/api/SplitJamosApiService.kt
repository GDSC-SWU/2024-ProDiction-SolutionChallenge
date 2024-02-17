package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SplitJamosApiService {
    @GET("/splitjamos")
    fun splitJamos(
        @Query("text") text: String
    ): Call<String>
}