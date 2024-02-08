package com.example.pro_diction.presentation.learn.phoneme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.pro_diction.R
import com.example.pro_diction.databinding.ActivityLearnPhonemeDetailBinding
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.VideoActivity

class LearnPhonemeDetailActivity : AppCompatActivity() {
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLearnPhonemeDetailBinding.inflate(layoutInflater)
    }
    private var item: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // textview text
        item = intent.getStringExtra("item")
        val tv = findViewById<TextView>(R.id.textView)
        tv.text = item

        // video click
        val video1 = findViewById<ImageView>(R.id.iv_video1)
        val video2 = findViewById<ImageView>(R.id.iv_video2)
        video1.setOnClickListener {
            val videoIntent = Intent(this@LearnPhonemeDetailActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", item)
            videoIntent.putExtra("videoType", "1")
            startActivity(videoIntent)
        }
        video2.setOnClickListener {
            val videoIntent = Intent(this@LearnPhonemeDetailActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", item)
            videoIntent.putExtra("videoType", "2")
            startActivity(videoIntent)
        }


        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
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