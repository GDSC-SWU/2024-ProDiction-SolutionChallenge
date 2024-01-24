package com.example.pro_diction.presentation.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.pro_diction.R

class OnBoarding2Activity : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding2)

        button = findViewById(R.id.btn_onboarding_2)



    }
}