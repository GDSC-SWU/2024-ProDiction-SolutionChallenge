package com.example.pro_diction.presentation.learn.sentense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.PhraseDetailDto
import com.example.pro_diction.data.dto.SentenseDetailDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.phrase.PhraseDetailAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LearnSentenseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_sentense_detail)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // consonant setting 자음 설정
        val main = intent.getStringExtra("main")
        var tvMain = findViewById<TextView>(R.id.tv_sentense_detail_main)
        tvMain.text = main

        // recycler view
        // main 이 뭔지에 따라서 리스트에 넣는 값이 달라짐
        val recyclerview = findViewById<RecyclerView>(R.id.rv_sentense_detail)
        val sentenseDetailList: MutableList<SentenseDetailDto> = mutableListOf()
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요? 성함이 어떻게 되시나요? 성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요? 안녕하세요? 만나서 반가워요. 저는 글을 길게 쓰면 어떻게 될지 테스트 중입니다."))
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))
        sentenseDetailList.add(SentenseDetailDto("안녕하세요"))
        sentenseDetailList.add(SentenseDetailDto("성함이 어떻게 되시나요?"))



        // 어댑터에 리스트 연결
        val adapter = SentenseDetailAdapter(sentenseDetailList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this@LearnSentenseDetailActivity)

        val intent = Intent(this@LearnSentenseDetailActivity, LearnPhonemeDetailActivity::class.java)
        // item 클릭 시 해당 음소 페이지로 연결
        adapter.setOnItemClickListener(object: SentenseDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", sentenseDetailList[position].sentenseDetail)
                startActivity(intent)
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_sentense_detail)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
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
