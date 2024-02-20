package com.example.pro_diction

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pro_diction.presentation.comm.CommFragment
import com.example.pro_diction.databinding.ActivityMainBinding
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.language.bm.Lang
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.itemIconTintList = null
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


/*
    override fun onResume() {
        super.onResume()
        val lang = getSharedPreferences("Settings", Context.MODE_PRIVATE).getString("My_Lang", "en")        // 변경된 언어 설정에 따라 화면 다시 로딩
        val config = Configuration() //이 클래스는 응용 프로그램이 검색하는 리소스에 영향을 줄 수 있는
        // 모든 장치 구성 정보를 설명합니다.
        val locale = Locale(lang)
        Locale.setDefault(locale)
    }*/
/*
    private fun reloadFragment(language: String) {
        // 프래그먼트에 언어 설정을 전달하고 화면 다시 로딩
        val fragment = MyFragment.newInstance(language)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    private fun saveLanguage(language: String) {
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("My_Lang", language)
        editor.apply()
    }

    private fun getSavedLanguage(): String {
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        return sharedPreferences.getString("My_Lang", "") ?: ""
    }*/
}