package com.example.pro_diction.presentation.learn.word

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.WordDetailDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.ConsonantAdapter
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LearnWordDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_word_detail)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // consonant setting 자음 설정
        val main = intent.getStringExtra("main")
        var tvMain = findViewById<TextView>(R.id.tv_word_detail_main)
        tvMain.text = main

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_word_detail)
        val wordDetailList: MutableList<WordDetailDto> = mutableListOf()
        wordDetailList.add(WordDetailDto("가방"))
        wordDetailList.add(WordDetailDto("가위"))
        wordDetailList.add(WordDetailDto("가운데"))
        wordDetailList.add(WordDetailDto("고라니"))

        // 어댑터에 리스트 연결
        val adapter = WordDetailAdapter(wordDetailList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this@LearnWordDetailActivity, 3)

        val intent = Intent(this@LearnWordDetailActivity, LearnPhonemeDetailActivity::class.java)
        // item 클릭 시 해당 음소 페이지로 연결
        adapter.setOnItemClickListener(object: WordDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", wordDetailList[position].wordDetail)
                startActivity(intent)
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_word_detail)
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