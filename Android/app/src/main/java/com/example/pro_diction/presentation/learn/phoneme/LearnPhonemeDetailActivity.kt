package com.example.pro_diction.presentation.learn.phoneme

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.pro_diction.MediaRecorderActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.AiApiPool
import com.example.pro_diction.data.AiRetrofitPool
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.StudyItem
import com.example.pro_diction.databinding.ActivityLearnPhonemeDetailBinding
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.VideoActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.File
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class LearnPhonemeDetailActivity : AppCompatActivity() {
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLearnPhonemeDetailBinding.inflate(layoutInflater)
    }
    private var item: String? = null
    private var splitedItem: String = ""
    private val splitJamosService = AiApiPool.splitJamos

    // media record
    private var fileName: String = ""

    private var recordButton: MediaRecorderActivity.RecordButton? = null
    private var recorder: MediaRecorder? = null

    private var playButton: MediaRecorderActivity.PlayButton? = null
    private var player: MediaPlayer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    // test api
    private val getScoreService = ApiPool.getScore

    // stt syllable
    private val postSttSyllables = AiApiPool.sttSyllables

    // study id api
    var getStudyId = ApiPool.getStudyId


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == com.example.pro_diction.presentation.learn.phoneme.REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(com.example.pro_diction.presentation.learn.phoneme.LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        postSttSyllables()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // textview text
        item = intent.getStringExtra("item")
        val tv = findViewById<TextView>(R.id.textView)
        Log.e("item", item.toString())

        item?.toInt()?.let {
            getStudyId.getStudyId(it).enqueue(object: Callback<BaseResponse<StudyItem>>{
                override fun onResponse(
                    call: Call<BaseResponse<StudyItem>>,
                    response: Response<BaseResponse<StudyItem>>
                ) {
                    if (response.isSuccessful) {
                        tv.text = response.body()?.data?.content
                        Log.e("response.body()?.data?.content", response.body()?.data?.content.toString())
                        Log.e("tv.text", tv.text.toString())
                        splitedItem = response.body()?.data?.splitPronunciation.toString()
                        Log.e("splitedItem", splitedItem)
                    }
                }

                override fun onFailure(call: Call<BaseResponse<StudyItem>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }


        /*
        splitJamosService.splitJamos(item.toString()).enqueue(object: retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.e("body", response.body().toString())
                    response.body()?.let {
                        splitedItem = response.body().toString()
                    }
                }
                else {
                    Log.e("error", "실패")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message?.let { Log.e("error", it) } ?: "서버통신 실패(응답값 x)"
            }
        })*/

        // video click
        val video1 = findViewById<ImageView>(R.id.iv_video1)
        val video2 = findViewById<ImageView>(R.id.iv_video2)
        video1.setOnClickListener {
            val videoIntent = Intent(this@LearnPhonemeDetailActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitedItem)
            videoIntent.putExtra("videoType", "1")
            startActivity(videoIntent)
        }
        video2.setOnClickListener {
            val videoIntent = Intent(this@LearnPhonemeDetailActivity, VideoActivity::class.java)
            videoIntent.putExtra("item", splitedItem)
            videoIntent.putExtra("videoType", "2")
            startActivity(videoIntent)
        }


        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)


        // media record
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"
        Log.e("filename", fileName)
        var mStartRecording = true

        ActivityCompat.requestPermissions(this, permissions,
            com.example.pro_diction.presentation.learn.phoneme.REQUEST_RECORD_AUDIO_PERMISSION
        )

        // record
        var btnRecord = findViewById<ImageButton>(R.id.btn_record)

        btnRecord.setOnClickListener {

            onRecord(mStartRecording)
            when (mStartRecording) {
                true -> {
                    btnRecord.setImageResource(R.drawable.btn_listening)
                }
                false -> {
                    btnRecord.setImageResource(R.drawable.btn_listen)
                }
            }
            mStartRecording = !mStartRecording

        }


    }

    fun postSttSyllables() {
        Log.e("postSttSyllables", "true")
        val file = File(fileName)
        Log.e("file", file.toString())
        Log.e("fileName", fileName.toString())


        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        postSttSyllables.postSttSyllable(fileToUpload).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("response.isSuccessful   response.body()", response.toString())
                        Log.e("response.isSuccessful   response.body()", response.body().toString())
                    } else {
                        Log.e("response.isNotSuccessful   response.body()", response.toString())
                    }
                } else {
                    response.body()?.let { Log.e("recoed     Response", it.toString()) }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message?.let { Log.e("recoed     error", it) } ?: "서버통신 실패(응답값 x)"
            }
        })
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

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}