package com.example.pro_diction.presentation.learn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.pro_diction.R
import com.example.pro_diction.data.AiApiPool
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.WordApiDto
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.min

class LearnResultActivity : AppCompatActivity() {
    var score : String? = null
    var sttResult : String? = null
    var splitSttResult : String? = null
    var studyId : String? = null
    var name : String? = null
    var splitedItem : String? = null

    // join phoneme api
    var joinJamos = AiApiPool.joinJamos

    // add word api
    var postMyWord = ApiPool.postMyWord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_result)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // intent
        score = intent.getStringExtra("score")
        sttResult = intent.getStringExtra("sttResult")
        splitSttResult = intent.getStringExtra("splitSttResult")
        studyId = intent.getStringExtra("studyId")
        name = intent.getStringExtra("name")
        splitedItem = intent.getStringExtra("splitedItem")

        var tvCorrect = findViewById<TextView>(R.id.tv_correct_result)
        var tvYour = findViewById<TextView>(R.id.tv_your_result)
        var tvResult = findViewById<TextView>(R.id.tv_result)

        tvYour.text = "[     " + sttResult + "     ]"
        tvResult.text = "The accuracy of " + name + " pronunciation is " + score + "%"


        splitedItem?.let {
            joinJamos.joinJamos(it).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        tvCorrect.text = "[     " + response.body().toString() + "     ]"
                        highlightDifferentCharacters(response.body().toString(), sttResult.toString())
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("error", t.toString())                }
            })
        }

        // video click
        val video1 = findViewById<ImageView>(R.id.iv_your_video1)
        val video2 = findViewById<ImageView>(R.id.iv_your_video2)
        val video1_correct = findViewById<ImageView>(R.id.iv_correct_video1)
        val video2_correct = findViewById<ImageView>(R.id.iv_correct_video2)

        video1.setOnClickListener {
            val videoIntent = Intent(this@LearnResultActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitSttResult)
            videoIntent.putExtra("videoType", "1")
            startActivity(videoIntent)
        }
        video2.setOnClickListener {
            val videoIntent = Intent(this@LearnResultActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitSttResult)
            videoIntent.putExtra("videoType", "2")
            startActivity(videoIntent)
        }
        video1_correct.setOnClickListener {
            val videoIntent = Intent(this@LearnResultActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitedItem)
            videoIntent.putExtra("videoType", "1")
            startActivity(videoIntent)
        }
        video2_correct.setOnClickListener {
            val videoIntent = Intent(this@LearnResultActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitedItem)
            videoIntent.putExtra("videoType", "2")
            startActivity(videoIntent)
        }

        var retestBtn = findViewById<Button>(R.id.btn_retest)
        var addBtn = findViewById<Button>(R.id.btn_add)

        // word add button
        addBtn.setOnClickListener {
            postMyWord.postWord(studyId?.toInt() ?: 0).enqueue(object: Callback<BaseResponse<WordApiDto>> {
                override fun onResponse(
                    call: Call<BaseResponse<WordApiDto>>,
                    response: Response<BaseResponse<WordApiDto>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Toast.makeText(this@LearnResultActivity, "Word added.", Toast.LENGTH_SHORT).show()

                        }
                    }

                    if (response.code() == 400) {
                        Toast.makeText(this@LearnResultActivity, "Word already exists.", Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onFailure(call: Call<BaseResponse<WordApiDto>>, t: Throwable) {
                    Log.e("error", t.toString())
                }
            })
        }

        // retest button
        retestBtn.setOnClickListener {
            val intent = Intent(this@LearnResultActivity, LearnPhonemeDetailActivity::class.java)
            intent.putExtra("item", studyId.toString())
            Log.e("studyId", studyId.toString())
            startActivity(intent)
        }

    }

    fun highlightDifferentCharacters(original: String, modified: String) {
        // 두 문자열 중에서 길이가 더 짧은 문자열의 길이를 기준으로 반복합니다.
        val minLength = min(original.length, modified.length)

        // 문자열을 담을 StringBuilder를 생성합니다.
        val result = StringBuilder()

        // 각 문자열의 길이만큼 반복하면서 각 문자를 비교합니다.
        for (i in 0 until minLength) {
            val originalChar = original[i]
            val modifiedChar = modified[i]

            // 두 문자가 다른 경우에는 색상을 변경하여 추가합니다.
            if (originalChar != modifiedChar) {
                result.append("<font color=\"#FF0000\">${modifiedChar}</font>")
            } else {
                result.append(modifiedChar)
            }
        }

        // 두 문자열의 길이가 다른 경우 나머지 문자열을 추가합니다.
        if (original.length > modified.length) {
            result.append("<font color=\"#FF0000\">${original.substring(minLength)}</font>")
        } else if (original.length < modified.length) {
            result.append(modified.substring(minLength))
        }

        // 최종 결과를 로그로 출력합니다.
        println(result.toString())
    }


    // 툴바 메뉴 버튼 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // 툴바 메뉴 클릭 됐을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish()
                return true
            }
            R.id.menu_search -> { // 검색 버튼
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}