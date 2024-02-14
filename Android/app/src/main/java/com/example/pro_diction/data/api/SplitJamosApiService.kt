package com.example.pro_diction.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SplitJamosApiService {
    @GET("/splitjamos/{text}")
    fun splitJamos(@Path("text") text: String): Call<String>
}