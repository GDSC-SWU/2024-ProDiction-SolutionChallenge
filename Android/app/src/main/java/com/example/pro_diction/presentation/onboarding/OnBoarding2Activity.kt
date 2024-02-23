package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R

class OnBoarding2Activity : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding2)

        // take the test button
        button = findViewById(R.id.btn_onboarding_2)
        button.setOnClickListener {
            val intent = Intent(this, OnBoarding2_1Activity::class.java)
            startActivity(intent)
        }

        // skip button
        var btnSkip = findViewById<TextView>(R.id.tv_skip)
        btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}