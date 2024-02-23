package com.example.pro_diction.presentation.splash

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.R
import com.example.pro_diction.presentation.auth.MainLoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn

class MainSplashActivity : AppCompatActivity() {

    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        var viewpager = findViewById<ViewPager2>(R.id.viewpager_splash)
        viewpager.adapter = ViewPagerAdapter(getTitleList(), getList(), getStringList()) // 어댑터 생성
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        viewpager.setPageTransformer(ZoomOutPageTransformer()) // 애니메이션 적용

        val button: Button = findViewById(R.id.btn_splash)

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) { // 3번째 페이지인 경우
                    button.setBackgroundResource(R.drawable.bg_background_round_on)
                    button.setTextColor(Color.parseColor("#E8ECEF"))
                    button.isClickable = true

                } else {
                    button.setBackgroundResource(R.drawable.bg_background_round)
                    button.setTextColor(Color.parseColor("#B9B9B9"))
                    button.isClickable = false

                }
            }
        })

        button.setOnClickListener {
            val intent = Intent(this, MainLoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && App.prefs.getLoggedInBefore() == true) { // account != null || App.prefs.getLoggedInBefore() == true
            // 이미 로그인 되어있으면 바로 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.e("Logged", "${App.prefs.getLoggedIn()}")
        }
    }

    // 뷰 페이저에 들어갈 아이템
    private fun getTitleList(): ArrayList<String> {
        return arrayListOf("Comm", "Learn", "My")
    }
    private fun getList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.splash1, R.drawable.splash2, R.drawable.splash3)
    }
    private fun getStringList(): ArrayList<String> {
        return arrayListOf<String>(getString(R.string.splash1_sub), getString(R.string.splash2_sub), getString(R.string.splash3_sub))
    }

    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}