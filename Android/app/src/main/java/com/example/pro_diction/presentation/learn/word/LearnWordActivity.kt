package com.example.pro_diction.presentation.learn.word

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.presentation.learn.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.SyllableAdapter

class LearnWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_word)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_word)
        val wordList: MutableList<WordDto> = mutableListOf()

        wordList.add(WordDto("ㄱ", listOf("가위", "그림책", "가위")))
        wordList.add(WordDto("ㄴ", listOf("나무", "나무", "나무")))
        wordList.add(WordDto("ㄱ", listOf("가위", "그림책", "가위")))
        wordList.add(WordDto("ㄱ", listOf("가위", "그림책", "가위")))
        val adapter = WordAdapter(wordList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: WordAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@LearnWordActivity, LearnWordDetailActivity::class.java)
                intent.putExtra("main", wordList[position].wordTitle)
                startActivity(intent)
            }
        })
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