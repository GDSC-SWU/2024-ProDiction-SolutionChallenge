package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import retrofit2.Call
import retrofit2.Response

class OnBoarding1Activity : AppCompatActivity() {
    lateinit var editAge : EditText
    lateinit var btnNext: Button
    lateinit var btnSkip: TextView
    private val patchAgeService = ApiPool.patchAge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding1)

        editAge = findViewById(R.id.edit_onboarding_1)
        btnNext = findViewById<Button>(R.id.btn_onboarding_1)
        btnSkip = findViewById(R.id.tv_skip)

        btnNext.setBackgroundResource(R.drawable.bg_background_round)
        btnNext.setTextColor(Color.parseColor("#B9B9B9"))
        btnNext.isClickable = false

        addOnBackPressedCallback()

        editAge.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
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
            Log.e("btnNext", "click")
            if (editAge.text.toString().toInt() > 0 && editAge.text.toString().toInt() <= 110) {
                patchAgeService.patchAge(editAge.text.toString().toInt()).enqueue(object : retrofit2.Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Log.e("data", response.body().toString())
                                val intent = Intent(this@OnBoarding1Activity, OnBoarding2Activity::class.java)
                                startActivity(intent)
                            }

                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Toast.makeText(this@OnBoarding1Activity , "나이를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

            }
            else {
                Toast.makeText(this , "올바른 나이를 입력하세요.", Toast.LENGTH_SHORT).show()
                Log.e("btnNext", "올바른 나이를 입력하세요.")
            }
        }

        btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateButtonState(text: String, button: Button) {
        if (text.isNotEmpty()) {
            // EditText에 값이 있을 때 버튼의 색상과 텍스트 변경
            button.setBackgroundResource(R.drawable.bg_background_round_on)
            button.setTextColor(Color.parseColor("#E8ECEF"))
            button.isEnabled = true
        } else {
            // EditText에 값이 없을 때 버튼을 초기 상태로 변경
            button.setBackgroundResource(R.drawable.bg_background_round)
            button.setTextColor(Color.parseColor("#B9B9B9"))
            button.isClickable = false
        }
    }

    private fun addOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로 가기 버튼이 눌렸을 때 처리 동작
            }
        }

        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun patchAgeApi() {

    }
}