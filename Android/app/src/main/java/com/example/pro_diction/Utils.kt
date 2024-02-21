package com.example.pro_diction

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class Utils (context: Context) {
    private val PREFS = "prefs"
    private val ACCESS_TOKEN = "Access_Token"
    private val REFRESH_TOKEN = "Refresh_Token"
    private val LOGGED_IN = false
    private val LOGGED_IN_BEFORE = false
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    private val prefsEditor = prefs.edit()

    // stage
    private val STAGE = 1

    // level
    private var LEVEL = ""

    // member id
    private var MEMBER_ID: Int = 0
    
    // coach mark
    private var COACH = false


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

    fun setLoggedIn(loggedIn: Boolean) {
        prefsEditor.putBoolean("LOGGED_IN", loggedIn).apply()
    }

    fun getLoggedIn(): Boolean {
        return prefs.getBoolean("LOGGED_IN", false)
    }

    fun setLoggedInBefore(loggedInBefore: Boolean) {
        prefsEditor.putBoolean("LOGGED_IN_BEFORE", loggedInBefore).apply()
    }

    fun getLoggedInBefore() : Boolean {
        return prefs.getBoolean("LOGGED_IN_BEFORE", false)
    }

    fun signOut() {
        prefsEditor.remove(ACCESS_TOKEN)
        prefsEditor.remove(REFRESH_TOKEN)
        prefsEditor.putBoolean("LOGGED_IN", false)
        prefsEditor.putBoolean("LOGGED_IN_BEFORE", false)
    }

    fun setStage(stage: Int) {
        prefsEditor.putInt("STAGE", stage).apply()
    }

    fun getStage() : Int {
        return prefs.getInt("STAGE", 1)
    }

    fun setLevel(level: String) {
        prefsEditor.putString("LEVEL", level)
    }

    fun getLevel() : String {
        return prefs.getString("LEVEL", "").toString()
    }

    fun setMemberId(id: Int) {
        prefsEditor.putInt("MEMBER_ID", id)
    }

    fun getMenberId() : Int {
        return prefs.getInt("MEMBER_ID", 0).toInt()
    }

    fun setCoach(coach: Boolean) {
        prefsEditor.putBoolean("COACH", coach)
    }

    fun getCoach() : Boolean {
        return prefs.getBoolean("COACH", false)
    }
    fun clearToken() {
        prefsEditor.clear().apply()
    }
}