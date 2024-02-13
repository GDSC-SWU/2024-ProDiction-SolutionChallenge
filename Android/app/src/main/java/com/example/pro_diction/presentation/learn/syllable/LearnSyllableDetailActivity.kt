package com.example.pro_diction.presentation.learn.syllable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.HangulConverter
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LearnSyllableDetailActivity : AppCompatActivity() {
    val getParentStudy = ApiPool.getParentStudy
    var list: MutableList<StudyResponseDto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_syllable_detail)

        val btnNull: Button = findViewById(R.id.btn_jong_null)
        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // selected text
        val studyId = intent.getStringExtra("studyId")
        val content = intent.getStringExtra("content")

        val tv = findViewById<TextView>(R.id.textView)
        tv.text = content

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_syllable_detail)
        val consonantList: MutableList<ConsonantDto> = mutableListOf()
        var adapter = SyllableDetailAdapter(list)


        // api
        getParentStudy.getParentStudyId(studyId?.toInt()!!).enqueue(object: retrofit2.Callback<BaseResponse<List<StudyResponseDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<StudyResponseDto>>>,
                response: Response<BaseResponse<List<StudyResponseDto>>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("LearnSyllableDetail", response.toString())
                        Log.e("LearnSyllableDetail", response.body()?.data.toString())
                        response.body()?.data?.forEach { it ->
                            list.add(it)
                        }

                        //adapter = SyllableDetailAdapter(list)
                        Log.e("LearnSyllableDetail", list.toString())
                        recyclerview.adapter = adapter
                        recyclerview.layoutManager = GridLayoutManager(this@LearnSyllableDetailActivity, 4)
                    }else {
                        Log.e("NotSuccessful", response.toString())
                    }

                } else {
                    response.body()?.let { Log.e("Response", it.toString()) }
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<StudyResponseDto>>>, t: Throwable) {
                Toast.makeText(this@LearnSyllableDetailActivity , "서버 통신을 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })



        // parser
        // val ja = consonantList
        // var result = ArrayList<String>()

        adapter = SyllableDetailAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this@LearnSyllableDetailActivity, 4)


        // onClick
        val intent = Intent(this@LearnSyllableDetailActivity, LearnPhonemeDetailActivity::class.java)
        adapter.setOnItemClickListener(object: SyllableDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // result.clear()
                //val list = parser.disassem(item) // 음소를 리스트로 받음, 이걸 받아서 또 다른 리스트로 넣어주고, 받침을 골랐을 때, 그걸 또 그 리스트에 넣은 후 한글 조합을 하여 하나의 글자로 만듦
                //result.addAll(list)
                //result.add(consonantList[position].item)
                //tv.text = parser.assem(result)
                // 종성 합치기

                /*
                val hangulChar = HangulConverter.joinHangulJongsung(item!!.get(0), consonantList[position].item[0])
                val intent = Intent(this@LearnSyllableDetailActivity, LearnPhonemeDetailActivity::class.java)
                intent.putExtra("item", hangulChar.toString())
                startActivity(intent)*/

                intent.putExtra("item", list[position].studyId.toString())
                startActivity(intent)

            }
        })

        btnNull.setOnClickListener {
            intent.putExtra("item", studyId)
            startActivity(intent)
        }
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