package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat.LOG_TAG
import androidx.core.view.isVisible
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.MainActivity2
import com.example.pro_diction.MediaRecorderActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.MyPageDto
import com.example.pro_diction.data.dto.RandomTestDto
import com.example.pro_diction.data.dto.TestScoreDto
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseDetailActivity
import com.github.squti.androidwaverecorder.RecorderState
import com.github.squti.androidwaverecorder.WaveRecorder
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.LogFactory.release
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.Locale
import java.util.concurrent.TimeUnit


class OnBoarding2_1Activity : AppCompatActivity() {
    var stage = App.prefs.getStage()// now stage
    private val getRandomTestService = ApiPool.getRandomTest
    var testList : MutableList<String> = mutableListOf()
    var testIdList : MutableList<Int> = mutableListOf()
    private var now = 0  // question number in this level
    private var total : Int = 0  // total score
    private var done = false  // if total < 60 => test done
    var testScoreList: MutableList<Int> = mutableListOf()

    // test api
    private val getScoreService = ApiPool.getScore

    // audio record
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77

    private lateinit var waveRecorder: WaveRecorder
    private lateinit var filePath: String
    private var isRecording = false
    private var isPaused = false

    var recordExist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding21)


        // skip button
        var btnSkip = findViewById<TextView>(R.id.tv_skip)
        btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var level = findViewById<TextView>(R.id.tv_onboarding_2_sub_1)
        var question = findViewById<TextView>(R.id.tv_onboarding_question)
        var recordBtn = findViewById<ImageButton>(R.id.btn_onboarding_2_1)
        var tvRecord = findViewById<TextView>(R.id.tv_record_before)
        var btnNext = findViewById<ImageView>(R.id.btn_next_record)

        stage = App.prefs.getStage()

        // audio
        filePath = externalCacheDir?.absolutePath + "/audioFile.wav"

        waveRecorder = WaveRecorder(filePath)

        waveRecorder.onStateChangeListener = {
            when (it) {
                RecorderState.RECORDING -> startRecording()
                RecorderState.STOP -> stopRecording()
                RecorderState.PAUSE -> pauseRecording()
            }
        }

        // api
        getRandomTestService.getRandomTest(stage).enqueue(object : retrofit2.Callback<BaseResponse<List<RandomTestDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<RandomTestDto>>>,
                response: Response<BaseResponse<List<RandomTestDto>>>
            ) {
                if (response.isSuccessful) {
                    Log.e("body", response.body().toString())
                    response.body()?.let {
                        testList.clear()
                        testList.add((response.body()?.data?.get(0)?.content ?: null).toString())
                        testList.add((response.body()?.data?.get(1)?.content ?: null).toString())
                        testList.add((response.body()?.data?.get(2)?.content ?: null).toString())

                        testIdList.clear()
                        testIdList.add(((response.body()?.data?.get(0)?.studyId ?: null)!!))
                        testIdList.add((response.body()?.data?.get(1)?.studyId ?: null)!!)
                        testIdList.add((response.body()?.data?.get(2)?.studyId ?: null)!!)

                        level.text = "Level." + stage
                        question.text = testList[0]
                    }
                } else {
                    Log.e("error", "실패")
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<RandomTestDto>>>, t: Throwable) {
                t.message?.let { Log.e("error", it) } ?: "server error"
            }
        })

        // record button
        waveRecorder.noiseSuppressorActive = true
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



        btnNext.setOnClickListener {
            if (isRecording == false) {
                if (recordExist == true) {
                    if (now == 0) {

                        testScoreList.clear()

                        uploadFile(0)

                        question.text = testList[1]
                        now++
                        recordExist = false
                        findViewById<TextView>(R.id.tv_record_before).visibility = View.VISIBLE

                    }
                    else if (now == 1) {

                        uploadFile(1)

                        question.text = testList[2]
                        now++
                        recordExist = false
                        findViewById<TextView>(R.id.tv_record_before).visibility = View.VISIBLE

                    }
                    else if (now == 2) {

                        uploadFileCallback(2, object : UploadFileCallback {
                            override fun onUploadFileCompleted() {

                                // total score
                                total = testScoreList[0] + testScoreList[1] + testScoreList[2]
                                if (total >= 210) {
                                    stage++
                                    done = false
                                }
                                else {
                                    stage++
                                    done = true
                                }
                                val intent = Intent(this@OnBoarding2_1Activity, OnBoarding2_1FinishActivity::class.java)
                                App.prefs.setStage(stage-1) // stage save
                                // test name intent
                                intent.putExtra("test0", testList[0])
                                intent.putExtra("test1", testList[1])
                                intent.putExtra("test2", testList[2])
                                // test score intent
                                intent.putExtra("testScore0", testScoreList[0].toString())
                                intent.putExtra("testScore1", testScoreList[1].toString())
                                intent.putExtra("testScore2", testScoreList[2].toString())
                                intent.putExtra("done", done)
                                Log.e("done intent", done.toString())
                                startActivity(intent)

                                now = 0
                            }
                        })



                    }
                }
                else {
                    Toast.makeText(this@OnBoarding2_1Activity, "Please record it!", Toast.LENGTH_SHORT).show()
                }

            }
            else {
                Toast.makeText(this@OnBoarding2_1Activity, "You cannot switch screens while recording.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun getTestScore(i: Int) {
        /*
        getScoreService.getScore(
            testIdList[i],
            MultipartBody.Part.createFormData(
                "file",
                File(fileName).name,
                File(fileName).asRequestBody("audio/wav".toMediaTypeOrNull())
            )
        )
            .enqueue(object : retrofit2.Callback<BaseResponse<TestScoreDto>> {
                override fun onResponse(
                    call: Call<BaseResponse<TestScoreDto>>,
                    response: Response<BaseResponse<TestScoreDto>>
                ) {
                    if (response.isSuccessful) {
                        Log.e("body", response.body().toString())
                        response.body()?.let {
                            testScoreList.add((response.body()?.data?.score ?: 0.0f))
                        }
                    } else {
                        Log.e("error", "실패")
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<TestScoreDto>>,
                    t: Throwable
                ) {
                    t.message?.let { Log.e("error", it) } ?: "서버통신 실패(응답값 x)"
                }
            })
*/
    }

    private fun uploadFile(i: Int) {
        val file = File(filePath)
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        getScoreService.getScore(testIdList[i], fileToUpload).enqueue(object: Callback<BaseResponse<TestScoreDto>> {
            override fun onResponse(
                call: Call<BaseResponse<TestScoreDto>>,
                response: Response<BaseResponse<TestScoreDto>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("response", response.toString())
                        response.body()?.data?.let { it?.score?.let { it1 -> testScoreList.add(it1) } }
                    }
                    else {
                        Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    response.body()?.let { Log.v("Response", it.toString()) }
                }

            }

            override fun onFailure(call: Call<BaseResponse<TestScoreDto>>, t: Throwable) {
                Log.e("response", t.toString())
            }
        })

    }

    private fun uploadFileCallback(i: Int,  callback: UploadFileCallback) {
        val file = File(filePath)
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        getScoreService.getScore(testIdList[i], fileToUpload).enqueue(object: Callback<BaseResponse<TestScoreDto>> {
            override fun onResponse(
                call: Call<BaseResponse<TestScoreDto>>,
                response: Response<BaseResponse<TestScoreDto>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("response", response.toString())
                        response.body()?.data?.let { it?.score?.let { it1 -> testScoreList.add(it1) } }

                        callback.onUploadFileCompleted()
                    }
                    else {
                        Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    response.body()?.let { Log.v("Response", it.toString()) }
                }

            }

            override fun onFailure(call: Call<BaseResponse<TestScoreDto>>, t: Throwable) {
                Log.e("response", t.toString())
            }
        })

    }

    interface UploadFileCallback {
        fun onUploadFileCompleted()
    }

    private fun startRecording() {
        Log.d(OnBoarding2_1Activity.TAG, waveRecorder.audioSessionId.toString())
        isRecording = true
        isPaused = false
        //findViewById<TextView>(R.id.messageTextView).visibility = View.GONE
        //findViewById<TextView>(R.id.recordingTextView).text = "Recording..."
        findViewById<TextView>(R.id.tv_record_before).visibility = View.INVISIBLE
        findViewById<ImageButton>(R.id.btn_onboarding_2_1).setImageDrawable(resources.getDrawable(R.drawable.btn_listening))
        //findViewById<Button>(R.id.pauseResumeRecordingButton).text = "PAUSE"
        //findViewById<Button>(R.id.pauseResumeRecordingButton).visibility = View.VISIBLE
        //findViewById<TextView>(R.id.noiseSuppressorSwitch).isEnabled = false
        recordExist = true
    }

    private fun stopRecording() {
        isRecording = false
        isPaused = false
        //findViewById<TextView>(R.id.recordingTextView).visibility = View.GONE
        //findViewById<TextView>(R.id.messageTextView).visibility = View.VISIBLE
        //findViewById<Button>(R.id.pauseResumeRecordingButton).visibility = View.GONE
        //findViewById<Switch>(R.id.showAmplitudeSwitch).isChecked = false
        findViewById<ImageButton>(R.id.btn_onboarding_2_1).setImageDrawable(resources.getDrawable(R.drawable.btn_listen))
        Toast.makeText(this, getString(R.string.record_success), Toast.LENGTH_SHORT).show()
        //findViewById<Button>(R.id.button).text = "START"
        //findViewById<TextView>(R.id.noiseSuppressorSwitch).isEnabled = true


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
    /*
        override fun onStop() {
            super.onStop()
            recorder?.release()
            recorder = null
            player?.release()
            player = null
        }*/
}