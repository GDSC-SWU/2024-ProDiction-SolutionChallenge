package com.example.pro_diction.presentation.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.pro_diction.R
import com.example.pro_diction.presentation.learn.SearchActivity

class MyWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_word)

        val type = intent.getStringExtra("type")
        // toolbar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvType: TextView = findViewById(R.id.tv_myword_type)
        when(type) {
            "1" -> {
                val stringValue = getString(R.string.learn_1)
                tvType.text = stringValue
                Log.e("text", R.string.learn_1.toString())
            }
        }

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
