package com.example.pro_diction

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

class OnBoarding1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding1)

        var btnNext = findViewById<Button>(R.id.btn_onboarding_1)
        btnNext.isClickable = false

        btnNext.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
                // 텍스트 변경 전에 수행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중에 수행할 작업
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후에 수행할 작업
                updateButtonState(s.toString(), btnNext)
            }
        })

        btnNext.setOnClickListener {
            val intent = Intent(this, OnBoarding2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun updateButtonState(text: String, button: Button) {
        if (text.isNotEmpty()) {
            // EditText에 값이 있을 때 버튼의 색상과 텍스트 변경
            button.setBackgroundColor(Color.parseColor("@colors/main"))
            button.setTextColor(Color.parseColor("@color/sub3"))
            button.isClickable = true
        } else {
            // EditText에 값이 없을 때 버튼을 초기 상태로 변경
            button.setBackgroundColor(Color.parseColor("#F8F8F8"))
            button.setTextColor(Color.parseColor("#B9B9B9"))
            button.isClickable = false
        }
    }
}