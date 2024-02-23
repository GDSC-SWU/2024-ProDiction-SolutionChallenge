package com.example.pro_diction.presentation.my

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R
import java.util.Locale

class LanguageActivity : AppCompatActivity() {
    private lateinit var language_code: String
    private lateinit var configuration: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        // toolbar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // language setting
        var korean = findViewById<Button>(R.id.radio_ko)
        var english = findViewById<Button>(R.id.radio_en)

        // 저장된 언어 코드를 불러온다.
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            Log.d("로그", "language :"+language)
            language_code = language
        }

        /*
        // 저장된 언어코드에 따라 라디오 버튼을 체크해준다.
        if(language_code.equals("en") || language_code.equals("")){
            english.setChecked(true);
        }else{
            korean.setChecked(true);
        }*/

        configuration = Configuration()
        //한/국어 라디오 버튼 변경
        korean.setOnClickListener {
            setLocate("ko")
            //recreate()
            Log.e("ko", "ko")
            val intent = Intent(this@LanguageActivity, MainActivity::class.java)
            startActivity(intent)
            //configuration.locale = Locale.KOREA
            //resources.updateConfiguration(configuration,resources.displayMetrics)

        }
        // 영어 라디오 버튼 변경
        english.setOnClickListener {
            setLocate("en")
            Log.e("en", "en")
            val intent = Intent(this@LanguageActivity, MainActivity::class.java)
            startActivity(intent)
            //recreate()

            //configuration.locale = Locale.US
            //resources.updateConfiguration(configuration,resources.displayMetrics)

        }


    }

    //Locale 객체를 생성특정 지리적, 정치적 또는 문화적 영역을 나타냅니다.
    private fun setLocate(Lang: String) {
        Log.d("로그", "setLocate")
        val locale = Locale(Lang) // Local 객체 생성. 인자로는 해당 언어의 축약어가 들어가게 됩니다. (ex. ko, en)
        Locale.setDefault(locale) // 생성한 Locale로 설정을 해줍니다.

        val config = Configuration() //이 클래스는 응용 프로그램이 검색하는 리소스에 영향을 줄 수 있는
        // 모든 장치 구성 정보를 설명합니다.

        config.setLocale(locale) // 현재 유저가 선호하는 언어를 환경 설정으로 맞춰 줍니다.
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        // Shared에 현재 언어 상태를 저장해 줍니다.
        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()

        Log.e("ko", config.locales.toString())


    }


    // 툴바 메뉴 클릭 됐을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
}