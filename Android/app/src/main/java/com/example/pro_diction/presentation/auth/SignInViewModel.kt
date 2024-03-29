package com.example.pro_diction.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pro_diction.App
import com.example.pro_diction.coreui.view.UiState
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.RetrofitPool
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private val _accessToken: MutableLiveData<UiState<String>> = MutableLiveData()
    val accessToken: LiveData<UiState<String>> = _accessToken

    fun postGoogleLogin(token: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getSignIn.postSignIn(token)
        }.fold(
            {
                _accessToken.value = UiState.Success(it.data?.accessToken ?: "")
                RetrofitPool.setAccessTokenApi(it.data?.accessToken ?: "")
                Log.e("응답값1", it.data?.accessToken.toString())
                Log.e("응답값2", it.data?.memberId.toString())
                Log.e("응답값3", it.data?.refreshToken.toString())
                App.prefs.setAccessToken(it.data?.accessToken ?: "")
                App.prefs.setRefreshToken(it.data?.refreshToken ?: "")
                App.prefs.setMemberId(it.data?.memberId ?: 0)
                App.prefs.setLoggedIn(true) // 로그인 상태
                Log.e("it.data?.accessToken", it.data?.accessToken.toString())
                Log.e("it.data?.refreshToke", it.data?.refreshToken.toString())
                Log.e("accessToken", (App.prefs.getAccessToken("")).toString())
                Log.e("refreshToken", (App.prefs.getRefreshToken("")).toString())
            },
            {
                Log.e("dddd", it.message.toString())
                Log.e("dddd", it.cause.toString())
                Log.e("dddd", it.toString())
            }
        )
    }
}