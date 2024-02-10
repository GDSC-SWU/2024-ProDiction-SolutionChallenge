package com.example.pro_diction.presentation.learn.phrase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.PhraseDto
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.word.LearnWordDetailActivity
import com.example.pro_diction.presentation.learn.word.WordAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LearnPhraseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_phrase)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_phrase)
        val phraseList: MutableList<PhraseDto> = mutableListOf()

        phraseList.add(PhraseDto("ㄱ", listOf("귀여운 강아지", "가는 기차")))
        phraseList.add(PhraseDto("ㄴ", listOf("날아가는 비행기", "날아가는 비행기")))
        phraseList.add(PhraseDto("ㄷ", listOf("도망가는 사자", "도망가는 사자")))
        phraseList.add(PhraseDto("ㄱ", listOf("귀여운 강아지", "가는 기차")))
        phraseList.add(PhraseDto("ㄴ", listOf("날아가는 비행기", "날아가는 비행기")))
        phraseList.add(PhraseDto("ㄷ", listOf("도망가는 사자", "도망가는 사자")))
        phraseList.add(PhraseDto("ㄱ", listOf("귀여운 강아지", "가는 기차")))
        phraseList.add(PhraseDto("ㄴ", listOf("날아가는 비행기", "날아가는 비행기")))
        phraseList.add(PhraseDto("ㄷ", listOf("도망가는 사자", "도망가는 사자")))

        val adapter = PhraseAdapter(phraseList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: PhraseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@LearnPhraseActivity, LearnPhraseDetailActivity::class.java)
                intent.putExtra("main", phraseList[position].phraseTitle)
                startActivity(intent)
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_phrase)
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