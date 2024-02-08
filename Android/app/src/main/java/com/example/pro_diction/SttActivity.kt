package com.example.pro_diction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class SttActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var recodeStr: String
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

        var btn_sst = findViewById<Button>(R.id.btn_sst)
        btn_sst.setOnClickListener {
            startVoiceRecording()
        }
    }
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

}