package com.example.pro_diction

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.util.Locale

class SttActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var recodeStr: String
    private lateinit var tts: TextToSpeech
    var isFirst = true
    // permissions
    private val REQUIRED_PERMISSIONS = mutableListOf(
        android.Manifest.permission.RECORD_AUDIO).toTypedArray()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stt)

        // permission
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                //setupStreamingModePipeline()

                //glSurfaceView.post { startCamera() }
                //glSurfaceView.visibility = View.VISIBLE

                //initView()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@SttActivity, "권한을 다시 설정해주세요!", Toast.LENGTH_SHORT).show()
            }

        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(*REQUIRED_PERMISSIONS)
            .check()

        // sst 버튼
        var btn_sst = findViewById<Button>(R.id.btn_sst)
        btn_sst.setOnClickListener {
            startVoiceRecording()
        }

        initTTS()
        // tts 버튼 & edit text
        var btn_tts = findViewById<Button>(R.id.btn_tts)
        var edit_tts = findViewById<EditText>(R.id.edit_tts)
        btn_tts.setOnClickListener {
            speakOut(edit_tts.text.toString())
        }
    }
    // sst
    private fun startVoiceRecording() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@SttActivity)
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(intent)
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
        }

        override fun onBeginningOfSpeech() {
        }

        override fun onRmsChanged(rmsdB: Float) {
        }

        override fun onBufferReceived(buffer: ByteArray?) {
        }

        override fun onEndOfSpeech() {
        }

        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }

        override fun onResults(results: Bundle?) {
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            recodeStr = "" // 음성인식을 두번 연속 할 때 기존 인식된 텍스트를 초기화 하는 코드
            for (i in matches!!.indices) recodeStr += matches[i]
            Log.d("TEXT", "$recodeStr")
        }

        override fun onPartialResults(partialResults: Bundle?) {
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
        }
    }

    // tts
    private fun initTTS() {
        tts = TextToSpeech(this, textToSpeechListener)
    }

    private val textToSpeechListener: TextToSpeech.OnInitListener = TextToSpeech.OnInitListener {
        if(isFirst) {
            isFirst = false
            return@OnInitListener
        }
        if (it == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREA)
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                // 지원하지 않는 언어거나, 없는 언어일때
            } else {
                // 정상 인식 되었을 때 실행
            }
        }
    }
    private fun speakOut(text: String) {
        tts.setPitch(1F)
        tts.setSpeechRate(1F)
        tts.speak(text, TextToSpeech.QUEUE_ADD, null, "id1")
        Toast.makeText(applicationContext, "재생 중..", Toast.LENGTH_SHORT).show()
    }

}