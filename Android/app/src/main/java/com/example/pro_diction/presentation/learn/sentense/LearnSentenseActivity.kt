package com.example.pro_diction.presentation.learn.sentense

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
import com.example.pro_diction.data.dto.SentenseDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseDetailActivity
import com.example.pro_diction.presentation.learn.phrase.PhraseAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LearnSentenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_sentense)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_sentense)
        val sentenseList: MutableList<SentenseDto> = mutableListOf()

        sentenseList.add(SentenseDto("첫만남", listOf("안녕하세요", "성함이 어떻게 되시나요?")))
        sentenseList.add(SentenseDto("학교에서", listOf("다음 시간은 어떤 과목이야?", "내일부터 방학합니다.")))
        sentenseList.add(SentenseDto("직장에서", listOf("1시에 회의합니다.", "퇴근하겠습니다.")))
        sentenseList.add(SentenseDto("카페/음식점에서", listOf("포인트 적립해주세요.", "준비되면 불러드릴게요.")))
        sentenseList.add(SentenseDto("놀이공원에서", listOf("할인되는 카드 있나요?", "얼마나 기다려야 하나요?")))

        val adapter = SentenseAdapter(sentenseList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: SentenseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@LearnSentenseActivity, LearnSentenseDetailActivity::class.java)
                intent.putExtra("main", sentenseList[position].sentenseTitle)
                startActivity(intent)
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_sentense)
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
