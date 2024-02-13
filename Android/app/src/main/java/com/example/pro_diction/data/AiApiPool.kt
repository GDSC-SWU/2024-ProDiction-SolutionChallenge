package com.example.pro_diction.data

import com.example.pro_diction.data.api.SplitJamosApiService
import com.example.pro_diction.data.api.SttSyllablesApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

object AiApiPool {
    val splitJamos = AiRetrofitPool.retrofit.create(SplitJamosApiService::class.java)
    val sttSyllables = AiRetrofitPool.retrofit.create(SttSyllablesApiService::class.java)
}

object AiRetrofitPool {
    private const val BASE_URL = "http://34.47.82.224:8000"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}