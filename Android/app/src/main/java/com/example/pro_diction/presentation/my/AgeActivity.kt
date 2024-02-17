package com.example.pro_diction.presentation.my

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.presentation.onboarding.OnBoarding2Activity
import retrofit2.Call
import retrofit2.Response

class AgeActivity : AppCompatActivity() {
    lateinit var editAge : EditText
    lateinit var btnNext: Button
    private val patchAgeService = ApiPool.patchAge
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age)

        editAge = findViewById(R.id.edit_age)
        btnNext = findViewById<Button>(R.id.btn_age)

        btnNext.setBackgroundResource(R.drawable.bg_background_round_on)

        var age = intent.getStringExtra("age")

        if (age != null) {
            editAge.setText(age)
        }

        editAge.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 텍스트 변경 전에 수행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중에 수행할 작업
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후에 수행할 작업
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
                                finish()
                            }

                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Toast.makeText(this@AgeActivity , "나이를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

            }
            else {
                Toast.makeText(this , "올바른 나이를 입력하세요.", Toast.LENGTH_SHORT).show()
                Log.e("btnNext", "올바른 나이를 입력하세요.")
            }
        }

    }

}