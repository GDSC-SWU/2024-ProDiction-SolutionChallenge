package com.example.pro_diction.data

import android.util.Log
import com.example.pro_diction.App
import com.example.pro_diction.data.api.SignInApiService
import com.example.pro_diction.data.API.API_TAG
import com.example.pro_diction.data.api.AddWordApiService
import com.example.pro_diction.data.api.CategoryApiService
import com.example.pro_diction.data.api.DeleteWordApiService
import com.example.pro_diction.data.api.LogoutApiService
import com.example.pro_diction.data.api.MyPageApiService
import com.example.pro_diction.data.api.MyWordApiService
import com.example.pro_diction.data.api.OnBoardingAgeApiService
import com.example.pro_diction.data.api.OnBoardingRandomApiService
import com.example.pro_diction.data.api.OnBoardingStageApiService
import com.example.pro_diction.data.api.OnBoardingTestApiService
import com.example.pro_diction.data.api.ParentStudyApiService
import com.example.pro_diction.data.api.StudyIdApiService
import com.example.pro_diction.data.api.SubCategoryApiService
import com.example.pro_diction.data.api.TestResultApiService
import com.example.pro_diction.data.api.TokenRefreshApiService
import com.example.pro_diction.data.dto.ResponseSignInDto
import com.example.pro_diction.data.dto.TestResultDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiPool {
    val getSignIn = RetrofitPool.retrofit.create(SignInApiService::class.java)
    val getTokenRefresh = RetrofitPool.retrofit.create(TokenRefreshApiService::class.java)
    val patchAge = RetrofitPool.retrofit.create(OnBoardingAgeApiService::class.java)
    val getRandomTest = RetrofitPool.retrofit.create(OnBoardingRandomApiService::class.java)
    val postStage = RetrofitPool.retrofit.create(OnBoardingStageApiService::class.java)
    val getScore = RetrofitPool.retrofit.create(OnBoardingTestApiService::class.java)
    val getCategory = RetrofitPool.retrofit.create(CategoryApiService::class.java)
    val getSubCategory = RetrofitPool.retrofit.create(SubCategoryApiService::class.java)
    val getParentStudy = RetrofitPool.retrofit.create(ParentStudyApiService::class.java)
    val getStudyId = RetrofitPool.retrofit.create(StudyIdApiService::class.java)
    val postTestResult = RetrofitPool.retrofit.create(TestResultApiService::class.java)
    val postMyWord = RetrofitPool.retrofit.create(AddWordApiService::class.java)
    val getMyWord = RetrofitPool.retrofit.create(MyWordApiService::class.java)
    val deleteMyWord = RetrofitPool.retrofit.create(DeleteWordApiService::class.java)
    val getMyPage = RetrofitPool.retrofit.create(MyPageApiService::class.java)
    val logout = RetrofitPool.retrofit.create(LogoutApiService::class.java)
}

object RetrofitPool {
    // AccessToken을 관리하는 변수 (로그인 이후 여기에 AccessToken을 저장한다고 가정)
    private var accessToken: String? = null
    //private var accessToken = App.prefs.getAccessToken("")

    // check refresh 리프레시 중인지 확인
    private var isRefresh: Boolean = false

    // AccessToken을 설정하는 함수
    fun setAccessTokenApi(token: String?) {
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
                    Log.e("1", accessToken.toString())
                    Log.e("2", App.prefs.getAccessToken("").toString())
                    Log.e("3", App.prefs.getRefreshToken("").toString())
                val request = App.prefs.getAccessToken("")?.let { token ->
                    if (token != "") {
                        if(!isRefresh) {
                            Log.e("4", "!isRefresh accessToken")

                            chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                        }
                        else {
                            Log.e("5", "isRefresh accessToken")

                            chain.request().newBuilder()
                                .addHeader("Authorization-refresh", "Bearer $token")
                                .build()
                        }
                    }
                    else {
                        chain.request().newBuilder()
                            .build()
                    }

                } ?: chain.request()

                var response = chain.proceed(request)

                // 토큰 만료 에러 발생시
                if (response.code == 401) {
                    Log.e("6", response.toString())
                    Log.e("7", App.prefs.getAccessToken("").toString())
                    Log.e("8", App.prefs.getRefreshToken("").toString())
                    runBlocking {
                        //  accessToken 재발급 api 요청
                        val refreshToken = App.prefs.getRefreshToken("")
                        isRefresh = true
                        if (refreshToken != null) {
                            accessToken = refreshToken
                            App.prefs.setAccessToken(refreshToken.toString())
                            val refreshResponse : BaseResponse<ResponseSignInDto> = ApiPool.getTokenRefresh.getTokenRefresh()


                            // accessToken 재발급 api가 성공적으로 응답이 왔을 때
                            if (refreshResponse.data!=null) {
                                isRefresh = false
                                App.prefs.setAccessToken(refreshResponse.data.accessToken)
                                Log.e("9", refreshResponse.data.accessToken)

                                App.prefs.setRefreshToken(refreshResponse.data.refreshToken)
                                Log.e("10", refreshResponse.data.refreshToken)

                                accessToken = App.prefs.getAccessToken("")
                                Log.e("11", accessToken.toString())


                                // 새로운 accessToken을 Request Header에 추가하고, 기존에 요청하고자 했던 api를 재요청
                                val refreshRequest = App.prefs.getAccessToken("")?.let { token ->
                                    Log.e("12", accessToken.toString())
                                    chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer $token")
                                        .build()
                                } ?: chain.request()
                                response.close()
                                response = chain.proceed(refreshRequest)
                                Log.e("13", accessToken.toString())
                                Log.e("14", App.prefs.getAccessToken("").toString())
                                Log.e("15", App.prefs.getRefreshToken("").toString())
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