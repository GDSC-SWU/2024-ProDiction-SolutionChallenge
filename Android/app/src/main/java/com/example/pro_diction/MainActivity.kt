package com.example.pro_diction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pro_diction.presentation.comm.CommFragment
import com.example.pro_diction.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigationView()

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.fragment_learn
        }


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

                else -> false
            }
        }
    }
}