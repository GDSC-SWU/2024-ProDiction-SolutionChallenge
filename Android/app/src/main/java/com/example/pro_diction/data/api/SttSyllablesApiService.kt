package com.example.pro_diction.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SttSyllablesApiService {
    @Multipart
    @POST("/speechtotext_syllables/")
    fun postSttSyllable(
        @Part file: MultipartBody.Part
    ): Call<String>
}