package com.example.pro_diction.data

import android.util.Log
import com.example.pro_diction.App
import com.example.pro_diction.data.api.SignInApiService
import com.example.pro_diction.data.API.API_TAG
import com.example.pro_diction.data.api.OnBoardingAgeApiService
import com.example.pro_diction.data.api.TokenRefreshApiService
import com.example.pro_diction.data.dto.ResponseSignInDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiPool {
    val getSignIn = RetrofitPool.retrofit.create(SignInApiService::class.java)
    val getTokenRefresh = RetrofitPool.retrofit.create(TokenRefreshApiService::class.java)
    val patchAge = RetrofitPool.retrofit.create(OnBoardingAgeApiService::class.java)
}

object RetrofitPool {
    // AccessToken을 관리하는 변수 (로그인 이후 여기에 AccessToken을 저장한다고 가정)
    private var accessToken: String? = null
    //private var accessToken = App.prefs.getAccessToken("")

    // check refresh 리프레시 중인지 확인
    private var isRefresh: Boolean = false

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
            OkHttpClient.Builder().connectTimeout( 100, TimeUnit.SECONDS )
                .readTimeout( 100, TimeUnit.SECONDS )
                .writeTimeout( 100, TimeUnit.SECONDS ).addInterceptor(loggingInterceptor).addInterceptor { chain ->

                // AccessToken이 있는 경우, 헤더에 추가합니다.
                    Log.e("access accessToken", accessToken.toString())
                    Log.e("refresh accessToken", accessToken.toString())
                    Log.e("access", App.prefs.getAccessToken("").toString())
                    Log.e("refresh", App.prefs.getRefreshToken("").toString())
                val request = accessToken?.let { token ->
                    if(!isRefresh) {
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                    }
                    else {
                        chain.request().newBuilder()
                            .addHeader("Authorization-refresh", "Bearer $token")
                            .build()
                    }
                } ?: chain.request()

                var response = chain.proceed(request)

                // 토큰 만료 에러 발생시
                if (response.code == 401) {
                    Log.e("response", response.toString())
                    Log.e("last access", App.prefs.getAccessToken("").toString())
                    Log.e("last refresh", App.prefs.getRefreshToken("").toString())
                    runBlocking {
                        //  accessToken 재발급 api 요청
                        val refreshToken = App.prefs.getRefreshToken("")
                        isRefresh = true
                        if (refreshToken != null) {
                            accessToken = refreshToken
                            val refreshResponse : BaseResponse<ResponseSignInDto> = ApiPool.getTokenRefresh.getTokenRefresh()


                            // accessToken 재발급 api가 성공적으로 응답이 왔을 때
                            if (refreshResponse.data!=null) {
                                isRefresh = false
                                App.prefs.setAccessToken(refreshResponse.data.accessToken)
                                App.prefs.setRefreshToken(refreshResponse.data.refreshToken)
                                accessToken = App.prefs.getAccessToken("")

                                // 새로운 accessToken을 Request Header에 추가하고, 기존에 요청하고자 했던 api를 재요청
                                val refreshRequest = accessToken?.let { token ->
                                    chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer ${App.prefs.getAccessToken("")}")
                                        .build()
                                } ?: chain.request()
                                response.close()
                                response = chain.proceed(refreshRequest)
                                Log.e("now access accessToken", accessToken.toString())
                                Log.e("now refresh accessToken", accessToken.toString())
                                Log.e("now access", App.prefs.getAccessToken("").toString())
                                Log.e("now refresh", App.prefs.getRefreshToken("").toString())
                            }
                            else {
                                Log.e("refreshResponse.data : 서버 통신", "refreshResponse.data == null")
                            }
                        }
                        else {
                            Log.e("refreshToken", "refreshToken == null")
                        }
                        /*
                        val refreshRequest = refreshToken?.let { refreshToken ->
                            chain.request().newBuilder()
                                .addHeader("Authorization-refresh", "Bearer $refreshToken")
                                .build()
                        } ?: chain.request()*/
                    }
                    response
                } else {
                    response
                }
            }.build()

        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://35.216.25.233:8080")
            .client(okHttpClient)
            .build()
    }
}



object API {
    const val API_TAG = "Retrofit2"
}