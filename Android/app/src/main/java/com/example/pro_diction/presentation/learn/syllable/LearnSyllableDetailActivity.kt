package com.example.pro_diction.presentation.learn.syllable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.HangulConverter
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity

class LearnSyllableDetailActivity : AppCompatActivity() {
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
        val item = intent.getStringExtra("item")
        val tv = findViewById<TextView>(R.id.textView)
        tv.text = item

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_syllable_detail)
        val consonantList: MutableList<ConsonantDto> = mutableListOf()
        consonantList.add(ConsonantDto("ㄱ"))
        consonantList.add(ConsonantDto("ㄴ"))
        consonantList.add(ConsonantDto("ㄷ"))
        consonantList.add(ConsonantDto("ㄹ"))
        consonantList.add(ConsonantDto("ㅁ"))
        consonantList.add(ConsonantDto("ㅂ"))
        consonantList.add(ConsonantDto("ㅅ"))
        consonantList.add(ConsonantDto("ㅇ"))
        consonantList.add(ConsonantDto("ㅈ"))
        consonantList.add(ConsonantDto("ㅊ"))
        consonantList.add(ConsonantDto("ㅋ"))
        consonantList.add(ConsonantDto("ㅌ"))
        consonantList.add(ConsonantDto("ㅍ"))
        consonantList.add(ConsonantDto("ㅎ"))
        consonantList.add(ConsonantDto("ㄲ"))
        consonantList.add(ConsonantDto("ㅆ"))
        consonantList.add(ConsonantDto("ㄳ"))
        consonantList.add(ConsonantDto("ㄵ"))
        consonantList.add(ConsonantDto("ㄶ"))
        consonantList.add(ConsonantDto("ㄺ"))
        consonantList.add(ConsonantDto("ㄻ"))
        consonantList.add(ConsonantDto("ㄼ"))
        consonantList.add(ConsonantDto("ㄽ"))
        consonantList.add(ConsonantDto("ㄾ"))
        consonantList.add(ConsonantDto("ㄿ"))
        consonantList.add(ConsonantDto("ㅀ"))
        consonantList.add(ConsonantDto("ㅄ"))



        val adapter = SyllableDetailAdapter(consonantList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this, 4)

        // parser
        val ja = consonantList
        var result = ArrayList<String>()


        // onClick
        adapter.setOnItemClickListener(object: SyllableDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // result.clear()
                //val list = parser.disassem(item) // 음소를 리스트로 받음, 이걸 받아서 또 다른 리스트로 넣어주고, 받침을 골랐을 때, 그걸 또 그 리스트에 넣은 후 한글 조합을 하여 하나의 글자로 만듦
                //result.addAll(list)
                //result.add(consonantList[position].item)
                //tv.text = parser.assem(result)
                // 종성 합치기
                val hangulChar = HangulConverter.joinHangulJongsung(item!!.get(0), consonantList[position].item[0])
                val intent = Intent(this@LearnSyllableDetailActivity, LearnPhonemeDetailActivity::class.java)
                intent.putExtra("item", hangulChar.toString())
                startActivity(intent)
            }
        })

        btnNull.setOnClickListener {
            val intent = Intent(this@LearnSyllableDetailActivity, LearnPhonemeDetailActivity::class.java)
            intent.putExtra("item", item)
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