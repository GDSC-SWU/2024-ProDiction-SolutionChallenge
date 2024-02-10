package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.RandomTestDto
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseDetailActivity
import retrofit2.Call
import retrofit2.Response

class OnBoarding2_1Activity : AppCompatActivity() {
    var stage = App.prefs.getStage()// 현재 레벨
    private val getRandomTestService = ApiPool.getRandomTest
    var testList : MutableList<String> = mutableListOf()
    var testIdList : MutableList<Int> = mutableListOf()
    private var now = 0  // 레벨에서 현재 몇 번째 문제인지
    private var total : Double = 0.0  // 총합 점수 (300점 만점에 60점 이상이면 테스트 지속)
    private var done = false  // (60점 이하면 테스트 중지)
    var testScoreList: List<Double> = listOf(10.0, 10.0, 10.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding21)

        var btnSkip = findViewById<TextView>(R.id.tv_skip)
        btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var level = findViewById<TextView>(R.id.tv_onboarding_2_sub_1)
        var question = findViewById<TextView>(R.id.tv_onboarding_question)
        var recordBtn = findViewById<ImageButton>(R.id.btn_onboarding_2_1)

        stage = App.prefs.getStage()

        // api
        getRandomTestService.getRandomTest(stage).enqueue(object : retrofit2.Callback<BaseResponse<List<RandomTestDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<RandomTestDto>>>,
                response: Response<BaseResponse<List<RandomTestDto>>>
            ) {
                if (response.isSuccessful) {
                    Log.e("body", response.body().toString())
                    response.body()?.let {
                        testList.clear()
                        testList.add((response.body()?.data?.get(0)?.content ?: null).toString())
                        testList.add((response.body()?.data?.get(1)?.content ?: null).toString())
                        testList.add((response.body()?.data?.get(2)?.content ?: null).toString())
                        testIdList.clear()
                        testIdList.add(((response.body()?.data?.get(0)?.studyId ?: null)!!))
                        testIdList.add((response.body()?.data?.get(1)?.studyId ?: null)!!)
                        testIdList.add((response.body()?.data?.get(2)?.studyId ?: null)!!)

                        level.text = "Level." + stage
                        question.text = testList[0]
                    }
                } else {
                    Log.e("error", "실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<RandomTestDto>>>, t: Throwable) {
                t.message?.let { Log.e("error", it) } ?: "서버통신 실패(응답값 x)"
            }
        })

        //
        recordBtn.setOnClickListener {
            if (now == 0) {
                // 백에 testIdList[0], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                question.text = testList[1]
                now++

            }
            else if (now == 1) {
                // 백에 testIdList[1], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                question.text = testList[2]
                now++

            }
            else if (now == 2) {
                // 백에 testIdList[2], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                // 점수 다 더해줌 계산
                total = testScoreList[0] + testScoreList[1] + testScoreList[2]
                if (total >= 60.0) {
                    stage++
                    done = false
                    // ai 돌리고 점수 저장
                }
                else {
                    stage++
                    done = true
                }
                val intent = Intent(this@OnBoarding2_1Activity, OnBoarding2_1FinishActivity::class.java)
                App.prefs.setStage(stage-1) // stage 저장
                // 각 단어 전달
                intent.putExtra("test0", testList[0])
                intent.putExtra("test1", testList[1])
                intent.putExtra("test2", testList[2])
                // 각각의 점수도 전달 합니다잉
                intent.putExtra("testScore0", testScoreList[0].toString())
                intent.putExtra("testScore1", testScoreList[1].toString())
                intent.putExtra("testScore2", testScoreList[2].toString())
                intent.putExtra("done", done)
                Log.e("done intent", done.toString())
                startActivity(intent)

                now = 0 // 여기서 저장해도 남아있나..? 아니면 객체 하나 만들어서 저장해주자 레벨도 같이 저장해도 될듯?

            }
        }
    }
}