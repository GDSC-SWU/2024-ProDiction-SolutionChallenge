package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JoinJamosApiService {
    @GET("/joinjamos")
    fun joinJamos(
        @Query("text") text : String
    ): Call<String>
}