package com.example.pro_diction

import android.util.Log
import com.example.pro_diction.API.API_TAG
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import kotlin.reflect.KProperty

object ApiPool {
    val getSignIn = RetrofitPool.retrofit.create(SignInApiService::class.java)
}

object RetrofitPool {
    // AccessToken을 관리하는 변수 (로그인 이후 여기에 AccessToken을 저장한다고 가정)
    private var accessToken: String? = null

    // AccessToken을 설정하는 함수
    fun setAccessToken(token: String?) {
        accessToken = token
    }

    val retrofit: Retrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            when {
                message.isJsonObject() -> Log.d(API_TAG, JSONObject(message).toString(4))

                message.isJsonArray() -> Log.d(API_TAG, JSONArray(message).toString(4))

                else -> {
                    Log.d(API_TAG, "CONNECTION INFO -> $message")
                }
            }
        }

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient =
            OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor { chain ->
                // AccessToken이 있는 경우, 헤더에 추가합니다.
                val request = accessToken?.let { token ->
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } ?: chain.request()

                chain.proceed(request)
            }.build()

        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://35.216.90.0:10000")
            .client(okHttpClient)
            .build()
    }
}



object API {
    const val API_TAG = "Retrofit2"
}