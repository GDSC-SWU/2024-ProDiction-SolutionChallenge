package com.example.pro_diction

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class Utils (context: Context) {
    private val PREFS = "prefs"
    private val ACCESS_TOKEN = "Access_Token"
    private val REFRESH_TOKEN = "Refresh_Token"
    // private val mContext: Context = context.applicationContext
    // private val prefs: SharedPreferences
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val prefsEditor = prefs.edit()
    // private var instance: Utils? = null


    /*
    companion object {
        // private const val PREFS = "prefs"
        // private const val ACCESS_TOKEN = "Access_Token"
        // private const val REFRESH_TOKEN = "Refresh_Token"
        private var instance: Utils? = null

        @Synchronized
        fun init(context: Context): Utils {
            if (instance == null) {
                instance = Utils(context)
            }
            return instance!!
        }

    }*/
/*
    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, "")
        set(value) = prefsEditor.putString(ACCESS_TOKEN, value).apply()

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, "")
        set(value) = prefsEditor.putString(REFRESH_TOKEN, value).apply()

*/
    fun setAccessToken(value: String) {
        prefsEditor.putString(ACCESS_TOKEN, value).apply()
    }

    fun getAccessToken(defValue: String): String? {
        return prefs.getString(ACCESS_TOKEN, defValue)
    }

    fun setRefreshToken(value: String) {
        prefsEditor.putString(REFRESH_TOKEN, value).apply()
    }

    fun getRefreshToken(defValue: String): String? {
        return prefs.getString(REFRESH_TOKEN, defValue)
    }

    fun clearToken() {
        prefsEditor.clear().apply()
    }
}