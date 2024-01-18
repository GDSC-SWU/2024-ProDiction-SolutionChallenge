package com.example.pro_diction.presentation

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

class TokenRefreshViewModel: ViewModel() {
    private val _accessToken: MutableLiveData<UiState<String>> = MutableLiveData()
    val accessToken: LiveData<UiState<String>> = _accessToken

    fun getTokenRefresh(token: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getTokenRefresh.getTokenRefresh(token)
        }.fold(
            {
                _accessToken.value = UiState.Success(it.data?.accessToken ?: "")
                // RetrofitPool.setAccessToken(it.data?.accessToken ?: "")
                Log.e("응답값1", it.data?.accessToken.toString())
                Log.e("응답값2", it.data?.memberId.toString())
                Log.e("응답값3", it.data?.refreshToken.toString())
                App.prefs.setAccessToken(it.data?.accessToken ?: "")
                App.prefs.setRefreshToken(it.data?.refreshToken ?: "")
            },
            {
                Log.e("TokenRefressh.kt", it.message.toString())
            }
        )
    }
}