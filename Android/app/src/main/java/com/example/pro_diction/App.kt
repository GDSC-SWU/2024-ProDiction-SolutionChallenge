package com.example.pro_diction

import android.app.Application

class App:Application() {
    companion object{
        lateinit var prefs : Utils
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Utils(applicationContext)
    }
}