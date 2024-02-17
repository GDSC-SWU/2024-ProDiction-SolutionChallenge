package com.example.pro_diction.presentation.learn.phoneme

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.pro_diction.MediaRecorderActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.AiApiPool
import com.example.pro_diction.data.AiRetrofitPool
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.StudyItem
import com.example.pro_diction.data.dto.TestResultDto
import com.example.pro_diction.data.dto.TestScoreDto
import com.example.pro_diction.databinding.ActivityLearnPhonemeDetailBinding
import com.example.pro_diction.presentation.learn.LearnResultActivity
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.VideoActivity
import com.example.pro_diction.presentation.onboarding.OnBoarding2_1Activity
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
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
    private var pronunciation = ""

    // media record
    private var fileName: String = ""


    // test api
    private val postTestResult = ApiPool.postTestResult

    // stt syllable
    private val postSttSyllables = AiApiPool.sttSyllables

    // study id api
    var getStudyId = ApiPool.getStudyId

    // audio record
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77

    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String
    private var isRecording = false
    private var isPaused = false

    var recordExist = false

    lateinit var tv: TextView

    var btnProClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        var recordBtn = findViewById<ImageButton>(R.id.btn_record)


        // audio
        filePath = externalCacheDir?.absolutePath + "/audioFile.wav"

        waveRecorder = WaveRecorder(filePath)
        waveRecorder.noiseSuppressorActive = true // noise remove
        //waveRecorder.waveConfig.sampleRate = 44100 // sample rate

        waveRecorder.onStateChangeListener = {
            when (it) {
                RecorderState.RECORDING -> startRecording()
                RecorderState.STOP -> stopRecording()
                RecorderState.PAUSE -> pauseRecording()
            }
        }

        // record button
        recordBtn.setOnClickListener {
            if (!isRecording) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.RECORD_AUDIO
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.RECORD_AUDIO),
                        PERMISSIONS_REQUEST_RECORD_AUDIO
                    )
                } else {
                    waveRecorder.startRecording()
                }
            } else {
                waveRecorder.stopRecording()
            }

        }

        // textview text
        item = intent.getStringExtra("item")
        tv = findViewById<TextView>(R.id.textView)
        Log.e("item", item.toString())


        item?.toInt()?.let {
            getStudyId.getStudyId(it).enqueue(object : Callback<BaseResponse<StudyItem>> {
                override fun onResponse(
                    call: Call<BaseResponse<StudyItem>>,
                    response: Response<BaseResponse<StudyItem>>
                ) {
                    if (response.isSuccessful) {
                        tv.text = response.body()?.data?.content
                        Log.e(
                            "response.body()?.data?.content",
                            response.body()?.data?.content.toString()
                        )
                        Log.e("tv.text", tv.text.toString())
                        splitedItem = response.body()?.data?.splitPronunciation.toString()
                        Log.e("splitedItem", splitedItem)
                        pronunciation = response.body()?.data?.pronunciation.toString()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<StudyItem>>, t: Throwable) {
                    Log.e("error", t.toString())
                }
            })
        }

        // view pronunciation
        var btnPro = findViewById<Button>(R.id.btn_pro)
        btnPro.setOnClickListener{
            if(btnProClicked == false) {
                btnPro.text = pronunciation
                btnPro.background = getDrawable(R.drawable.bg_background_round_on_border)
                btnPro.setTextColor(Color.parseColor("#2F4C74"))
                btnProClicked = true
            }
            else {
                btnPro.text = getString(R.string.view_pro)
                btnPro.background = getDrawable(R.drawable.bg_background_round_on)
                btnPro.setTextColor(Color.parseColor("#FFFFFF"))
                btnProClicked = false
            }
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


    }
    fun uploadFile() {
        val file = File(filePath)
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        item?.let { postTestResult.postResult(it.toInt(),fileToUpload).enqueue(object : Callback<BaseResponse<TestResultDto>> {
            override fun onResponse(
                call: Call<BaseResponse<TestResultDto>>,
                response: Response<BaseResponse<TestResultDto>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        if (response.body()!!.data?.sttResult.toString() == "") {
                            Toast.makeText(this@LearnPhonemeDetailActivity, "Please record again.", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.body()!!.data?.sttResult.toString() == "too fast"){
                            Toast.makeText(this@LearnPhonemeDetailActivity, "Please pronounce syllable by syllable slowly and clearly.", Toast.LENGTH_SHORT).show()
                        }
                        else if('-' in response.body()!!.data?.sttResult.toString()) {
                            Toast.makeText(this@LearnPhonemeDetailActivity, "Please pronounce syllable by syllable slowly and clearly.", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.body()!!.data?.sttResult.toString().contains("Read timed out")) {
                            Toast.makeText(this@LearnPhonemeDetailActivity, "Timed out.", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            val resultIntent = Intent(this@LearnPhonemeDetailActivity, LearnResultActivity::class.java)
                            resultIntent.putExtra("score", response.body()!!.data?.score.toString())
                            resultIntent.putExtra("sttResult", response.body()!!.data?.sttResult.toString())
                            resultIntent.putExtra("splitSttResult", response.body()!!.data?.splitSttResult.toString())
                            resultIntent.putExtra("studyId", response.body()!!.data?.studyId.toString())
                            resultIntent.putExtra("name", tv.text)
                            resultIntent.putExtra("splitedItem", splitedItem.toString())
                            startActivity(resultIntent)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<TestResultDto>>, t: Throwable) {
                Log.e("error", t.toString())
            }
        })
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

    private fun startRecording() {
        //Log.d(OnBoarding2_1Activity.TAG, waveRecorder.audioSessionId.toString())
        isRecording = true
        isPaused = false

        findViewById<TextView>(R.id.tv_learn_record_before).visibility = View.INVISIBLE
        findViewById<ImageButton>(R.id.btn_record).setImageDrawable(resources.getDrawable(R.drawable.btn_listening))

        recordExist = true
    }

    private fun stopRecording() {
        isRecording = false
        isPaused = false

        findViewById<ImageButton>(R.id.btn_record).setImageDrawable(resources.getDrawable(R.drawable.btn_listen))
        Toast.makeText(this, "Recording completed", Toast.LENGTH_SHORT).show()

        if (isRecording == false) {
            if (recordExist == true) {
                uploadFile()
            }
        }
    }

    private fun pauseRecording() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    waveRecorder.startRecording()
                }
                return
            }

            else -> {
            }
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

    /*
    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }*/
}