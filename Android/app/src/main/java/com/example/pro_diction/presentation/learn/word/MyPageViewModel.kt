package com.example.pro_diction.presentation.learn.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pro_diction.data.ApiPool
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.pro_diction.App


class MyPageViewModel : ViewModel() {

    fun getMyPage() = viewModelScope.launch {
        runCatching {
            ApiPool.getMyPage.getMypage()
        }.fold(
            {
                //it.data
            },{

            }
        )
    }
}