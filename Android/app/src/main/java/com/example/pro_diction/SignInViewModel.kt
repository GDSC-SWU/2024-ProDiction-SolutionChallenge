package com.example.pro_diction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pro_diction.ApiPool
import com.example.pro_diction.RetrofitPool
import kotlinx.coroutines.launch
import com.example.pro_diction.UiState

class SignInViewModel : ViewModel() {
    private val _accessToken: MutableLiveData<UiState<String>> = MutableLiveData()
    val accessToken: LiveData<UiState<String>> = _accessToken

    fun postGoogleLogin(token: String) = viewModelScope.launch {
        runCatching {
            ApiPool.getSignIn.postSignIn(token)
        }.fold(
            {
                _accessToken.value = UiState.Success(it.data?.accessToken ?: "")
                RetrofitPool.setAccessToken(it.data?.accessToken ?: "")
            },
            {
                Log.e("dddd", it.message.toString())
            }
        )
    }
}