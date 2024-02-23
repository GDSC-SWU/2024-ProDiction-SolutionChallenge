package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R

class OnBoardingFinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_finish)

        var tvResult = findViewById<TextView>(R.id.tv_onboarding_finish)
        var stage = ""
        if (App.prefs.getStage() == 1) {
            stage = getString(R.string.learn_1)
        }
        else if (App.prefs.getStage() == 2) {
            stage = getString(R.string.learn_2)
        }
        else if (App.prefs.getStage() == 3) {
            stage = getString(R.string.learn_3)
        }
        else if (App.prefs.getStage() == 4) {
            stage = getString(R.string.learn_4)
        }
        else if (App.prefs.getStage() == 5) {
            stage = getString(R.string.learn_5)
        }
        else{

        }
        tvResult.text = getString(R.string.you_are) + " '" + stage + "' !"
        App.prefs.setLevel(stage)

        var btnStart = findViewById<Button>(R.id.btn_onboarding_finish)
        btnStart.setOnClickListener {
            val intent = Intent(this@OnBoardingFinishActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}