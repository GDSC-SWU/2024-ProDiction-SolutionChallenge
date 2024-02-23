package com.example.pro_diction.presentation.learn

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.pro_diction.R
import com.example.pro_diction.databinding.ActivityVideoBinding
import com.example.pro_diction.presentation.search.SearchActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util

class VideoActivity : AppCompatActivity() {
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityVideoBinding.inflate(layoutInflater)
    }
    private var player: SimpleExoPlayer?= null
    private var item: String? = null
    private var videoType: String? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        item = intent.getStringExtra("item")
        videoType = intent.getStringExtra("videoType")
        Log.e("splitedItem", item.toString())
        Log.e("videoType", videoType.toString())


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
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewBinding.videoView.player = exoPlayer
                if (videoType == "1") {
                    var baseUriV1 = getString(R.string.media_url_mp4_v1)

                    item?.forEachIndexed { index, i ->
                        if (index == 0) {
                            var uri1 = baseUriV1 + i + ".mp4"
                            Log.e("uri1", uri1)
                            val mediaItem = MediaItem.fromUri(uri1)
                            exoPlayer.setMediaItem(mediaItem)
                        }
                        else {
                            var uri2 = baseUriV1 + i + ".mp4"
                            Log.e("uri2", uri2)
                            val secondMediaItem = MediaItem.fromUri(uri2)
                            exoPlayer.addMediaItem(secondMediaItem)
                            /*
                            // 음절은 종성이 겹자음인지 검사 후 대치
                            if (item!!.length == 3 && index == 2) {
                                var jong = isDoubleJong(i.toString())
                                var uri2 = baseUriV1 + jong + ".mp4"
                                val secondMediaItem = MediaItem.fromUri(uri2)
                                exoPlayer.addMediaItem(secondMediaItem)
                                Log.e("uri2", uri2)
                            }
                            else {
                                var uri2 = baseUriV1 + i + ".mp4"
                                val secondMediaItem = MediaItem.fromUri(uri2)
                                exoPlayer.addMediaItem(secondMediaItem)
                                Log.e("uri2", uri2)
                            }*/
                        }
                    }

                }
                else if (videoType == "2") {
                    var baseUriV2 = getString(R.string.media_url_mp4_v2)

                    item?.forEachIndexed { index, i ->
                        if (index == 0) {
                            var uri1 = baseUriV2 + i + ".mp4"
                            val mediaItem = MediaItem.fromUri(uri1)
                            exoPlayer.setMediaItem(mediaItem)
                            Log.e("uri1", uri1)
                        }
                        else {
                            var uri2 = baseUriV2 + i + ".mp4"
                            val secondMediaItem = MediaItem.fromUri(uri2)
                            exoPlayer.addMediaItem(secondMediaItem)
                            Log.e("uri2", uri2)
                            /*
                            // 음절은 종성이 겹자음인지 검사 후 대치
                            if (item!!.length == 3 && index == 2) {
                                var jong = isDoubleJong(i.toString())
                                var uri2 = baseUriV2 + jong + ".mp4"
                                val secondMediaItem = MediaItem.fromUri(uri2)
                                exoPlayer.addMediaItem(secondMediaItem)
                                Log.e("uri2", uri2)
                            }
                            else {
                                var uri2 = baseUriV2 + i + ".mp4"
                                val secondMediaItem = MediaItem.fromUri(uri2)
                                exoPlayer.addMediaItem(secondMediaItem)
                                Log.e("uri2", uri2)
                            }*/
                        }
                    }

                }

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }

    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        viewBinding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    private fun isDoubleJong(string: String) : String {
        // 모든 받침 포함? ㄱㄴㄷㄹㅁㅂㅇ
        if (string == "ㄲ") {
            return "ㄱ"
        }
        else if (string == "ㅆ"){
            return "ㄷ"
        }
        else if (string == "ㄳ"){
            return "ㄱ"
        }
        else if (string == "ㄵ"){
            return "ㄴ"
        }
        else if (string == "ㄶ"){
            return "ㄴ"
        }
        else if (string == "ㄺ"){
            return "ㄱ"
        }
        else if (string == "ㄻ"){
            return "ㅁ"
        }
        else if (string == "ㄼ"){
            return "ㅂ"
        }
        else if (string == "ㄽ"){
            return "ㄹ"
        }
        else if (string == "ㄾ"){
            return "ㄹ"
        }
        else if (string == "ㄿ"){
            return "ㅂ"
        }
        else if (string == "ㅀ"){
            return "ㄹ"
        }
        else if (string == "ㅄ"){
            return "ㅂ"
        }
        else {
            return string
        }

    }
}