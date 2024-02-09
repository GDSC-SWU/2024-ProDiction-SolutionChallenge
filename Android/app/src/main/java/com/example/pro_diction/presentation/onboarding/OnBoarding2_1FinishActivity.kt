package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.App
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.dto.OnBoardingResultDto
import com.example.pro_diction.presentation.learn.word.LearnWordDetailActivity
import com.example.pro_diction.presentation.learn.word.WordAdapter
import retrofit2.Call
import retrofit2.Response

class OnBoarding2_1FinishActivity : AppCompatActivity() {
    private var finish = false
    private var postStage = ApiPool.postStage
    // testId 리스트로 만들고 점수와 함께 전달하기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding21_finish)

        var btnNext = findViewById<Button>(R.id.btn_onboarding_2_1_finish)
        var tvLevel = findViewById<TextView>(R.id.tv_onboarding_2_sub_1_finish)

        val test0 = intent.getStringExtra("test0")
        val test1 = intent.getStringExtra("test1")
        val test2 = intent.getStringExtra("test2")
        val testScore0 = intent.getStringExtra("testScore0")
        val testScore1 = intent.getStringExtra("testScore1")
        val testScore2 = intent.getStringExtra("testScore2")
        val done = intent.getBooleanExtra("done", false)

        Log.e("done", done.toString())

        // level on textView
        tvLevel.text = "Level." + ((App.prefs.getStage()).toString())

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_onboarding_result)
        val resultList: MutableList<OnBoardingResultDto> = mutableListOf()

        resultList.add(OnBoardingResultDto(test0.toString(), testScore0, testScore0))
        resultList.add(OnBoardingResultDto(test1.toString(), testScore1, testScore1))
        resultList.add(OnBoardingResultDto(test2.toString(), testScore2, testScore2))

        val adapter = OnBoardingResultAdapter(resultList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        // 글자와 프로그래스 색상 변경은 나중에 어댑터..?에서 처리

        // 단계가 5개가 끝난 경우 finish를 true로 설정
        if (App.prefs.getStage() == 5) {
            btnNext.text = getString(R.string.view_level)
            finish = true
        }
        
        // 총 점수 합이 60을 넘지 못한 경우
        if (done == true) {
            btnNext.text = getString(R.string.view_level)
            finish = true
            Log.e("btnNext.text", btnNext.text.toString())
            Log.e("getString(R.string.view_level)", getString(R.string.view_level))
        }

        btnNext.setOnClickListener {
            if (finish == true) {
                // 서버에 단계 저장  App.prefs.getStage()
                postStage.postStage(App.prefs.getStage()).enqueue(object : retrofit2.Callback<Int> {
                    override fun onResponse(call: Call<Int>, response: Response<Int>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                Log.e("stage", response.body().toString())
                                val intent = Intent(this@OnBoarding2_1FinishActivity, OnBoardingFinishActivity::class.java)
                                startActivity(intent)
                            }

                        }
                    }

                    override fun onFailure(call: Call<Int>, t: Throwable) {
                        Toast.makeText(this@OnBoarding2_1FinishActivity , "단계를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                // 다음 단계로 이동
                App.prefs.setStage(App.prefs.getStage()+1)
                val intent = Intent(this@OnBoarding2_1FinishActivity, OnBoarding2_1Activity::class.java)
                startActivity(intent)
            }
        }
    }
}