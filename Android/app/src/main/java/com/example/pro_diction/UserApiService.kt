package com.example.pro_diction

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("/api/user/login")
    suspend fun loginUser(@Body userDTO: UserDTO): Response<String>

    companion object {
        fun create(): UserApiService {
            return Retrofit.Builder()
                .baseUrl("YOUR_SPRING_BOOT_SERVER_BASE_URL")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApiService::class.java)
        }
    }
}