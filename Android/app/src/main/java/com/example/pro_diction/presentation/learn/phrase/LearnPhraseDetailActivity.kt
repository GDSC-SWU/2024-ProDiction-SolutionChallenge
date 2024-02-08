package com.example.pro_diction.presentation.learn.phrase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.PhraseDetailDto
import com.example.pro_diction.data.dto.WordDetailDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.word.WordDetailAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LearnPhraseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_phrase_detail)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // consonant setting 자음 설정
        val main = intent.getStringExtra("main")
        var tvMain = findViewById<TextView>(R.id.tv_phrase_detail_main)
        tvMain.text = main

        // recycler view
        // main 이 뭔지에 따라서 리스트에 넣는 값이 달라짐
        val recyclerview = findViewById<RecyclerView>(R.id.rv_phrase_detail)
        val phraseDetailList: MutableList<PhraseDetailDto> = mutableListOf()
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))
        phraseDetailList.add(PhraseDetailDto("귀여운 강아지"))
        phraseDetailList.add(PhraseDetailDto("가는 기차"))



        // 어댑터에 리스트 연결
        val adapter = PhraseDetailAdapter(phraseDetailList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this@LearnPhraseDetailActivity)

        val intent = Intent(this@LearnPhraseDetailActivity, LearnPhonemeDetailActivity::class.java)
        // item 클릭 시 해당 음소 페이지로 연결
        adapter.setOnItemClickListener(object: PhraseDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", phraseDetailList[position].phraseDetail)
                startActivity(intent)
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_phrase_detail)
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
