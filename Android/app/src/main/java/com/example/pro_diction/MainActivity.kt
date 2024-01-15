package com.example.pro_diction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pro_diction.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_comm      }
    }

    fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_comm -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, CommFragment()).commit()
                    true
                }
                R.id.fragment_learn -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, LearnFragment()).commit()
                    true
                }
                R.id.fragment_my -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, MyFragment()).commit()
                    true
                }
                R.id.fragment_setting -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, SettingFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }
}