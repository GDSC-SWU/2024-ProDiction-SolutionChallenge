package com.example.pro_diction

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.TestScoreDto
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity2 : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77

    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String
    private var isRecording = false
    private var isPaused = false

    var getScore = ApiPool.getScore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        filePath = externalCacheDir?.absolutePath + "/audioFile.wav"

        waveRecorder = WaveRecorder(filePath)

        waveRecorder.onStateChangeListener = {
            when (it) {
                RecorderState.RECORDING -> startRecording()
                RecorderState.STOP -> stopRecording()
                RecorderState.PAUSE -> pauseRecording()
            }
        }
        waveRecorder.onTimeElapsed = {
            Log.e(TAG, "onCreate: time elapsed $it")
            findViewById<TextView>(R.id.timeTextView).text = formatTimeUnit(it * 1000)
        }

        findViewById<Button>(R.id.button).setOnClickListener {

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

        findViewById<TextView>(R.id.pauseResumeRecordingButton).setOnClickListener {
            if (!isPaused) {
                waveRecorder.pauseRecording()
            } else {
                waveRecorder.resumeRecording()
            }
        }
        findViewById<Switch>(R.id.showAmplitudeSwitch).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                findViewById<TextView>(R.id.amplitudeTextView).text = "Amplitude : 0"
                findViewById<TextView>(R.id.amplitudeTextView).visibility = View.VISIBLE
                waveRecorder.onAmplitudeListener = {
                    GlobalScope.launch(Dispatchers.Main) {
                        findViewById<TextView>(R.id.amplitudeTextView).text = "Amplitude : $it"
                    }
                }

            } else {
                waveRecorder.onAmplitudeListener = null
                findViewById<TextView>(R.id.amplitudeTextView).visibility = View.GONE
            }
        }

        findViewById<Switch>(R.id.noiseSuppressorSwitch).setOnCheckedChangeListener { buttonView, isChecked ->
            waveRecorder.noiseSuppressorActive = isChecked
            if (isChecked)
                Toast.makeText(this, "Noise Suppressor Activated", Toast.LENGTH_SHORT).show()

        }
    }

    private fun startRecording() {
        Log.d(TAG, waveRecorder.audioSessionId.toString())
        isRecording = true
        isPaused = false
        findViewById<TextView>(R.id.messageTextView).visibility = View.GONE
        findViewById<TextView>(R.id.recordingTextView).text = "Recording..."
        findViewById<TextView>(R.id.recordingTextView).visibility = View.VISIBLE
        findViewById<Button>(R.id.button).text = "STOP"
        findViewById<Button>(R.id.pauseResumeRecordingButton).text = "PAUSE"
        findViewById<Button>(R.id.pauseResumeRecordingButton).visibility = View.VISIBLE
        findViewById<TextView>(R.id.noiseSuppressorSwitch).isEnabled = false
    }

    private fun stopRecording() {
        isRecording = false
        isPaused = false
        findViewById<TextView>(R.id.recordingTextView).visibility = View.GONE
        findViewById<TextView>(R.id.messageTextView).visibility = View.VISIBLE
        findViewById<Button>(R.id.pauseResumeRecordingButton).visibility = View.GONE
        findViewById<Switch>(R.id.showAmplitudeSwitch).isChecked = false
        Toast.makeText(this, "File saved at : $filePath", Toast.LENGTH_LONG).show()
        findViewById<Button>(R.id.button).text = "START"
        findViewById<TextView>(R.id.noiseSuppressorSwitch).isEnabled = true

        val file = File(filePath)
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        getScore.getScore(831, fileToUpload).enqueue(object: Callback<BaseResponse<TestScoreDto>> {
            override fun onResponse(
                call: Call<BaseResponse<TestScoreDto>>,
                response: Response<BaseResponse<TestScoreDto>>
            ) {
                Log.e("response", response.toString())
            }

            override fun onFailure(call: Call<BaseResponse<TestScoreDto>>, t: Throwable) {
                Log.e("response", t.toString())
            }
        })
    }

    private fun pauseRecording() {
        findViewById<TextView>(R.id.recordingTextView).text = "PAUSE"
        findViewById<Button>(R.id.pauseResumeRecordingButton).text = "RESUME"
        isPaused = true
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

    companion object {
        private const val TAG = "MainActivity"
    }

    private fun formatTimeUnit(timeInMilliseconds: Long): String {
        return try {
            String.format(
                Locale.getDefault(),
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds),
                TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds)
                )
            )
        } catch (e: Exception) {
            "00:00"
        }
    }
}