package com.example.pro_diction.presentation.onboarding

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.MediaRecorderActivity
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.RandomTestDto
import com.example.pro_diction.data.dto.TestScoreDto
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseDetailActivity
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

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class OnBoarding2_1Activity : AppCompatActivity() {
    var stage = App.prefs.getStage()// 현재 레벨
    private val getRandomTestService = ApiPool.getRandomTest
    var testList : MutableList<String> = mutableListOf()
    var testIdList : MutableList<Int> = mutableListOf()
    private var now = 0  // 레벨에서 현재 몇 번째 문제인지
    private var total : Float = 0.0f  // 총합 점수 (300점 만점에 60점 이상이면 테스트 지속)
    private var done = false  // (60점 이하면 테스트 중지)
    var testScoreList: MutableList<Float> = mutableListOf()

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
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
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
                Log.e(LOG_TAG, "prepare() failed")
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding21)

        // media record
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"
        Log.e("filename", fileName)
        var mStartRecording = true
        var recordExist = false

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

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
                t.message?.let { Log.e("error", it) } ?: "서버통신 실패(응답값 x)"
            }
        })

        //
        recordBtn.setOnClickListener {
            recordExist = true

            // 백에 testIdList[0], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
            onRecord(mStartRecording)
            when (mStartRecording) {
                true -> {
                    recordBtn.setImageResource(R.drawable.btn_listening)
                    tvRecord.isVisible = false
                    btnNext.isVisible = false
                }
                false -> {
                    recordBtn.setImageResource(R.drawable.btn_listen)
                    tvRecord.isVisible = true
                    btnNext.isVisible = true
                }
            }
            mStartRecording = !mStartRecording

        }

        btnNext.setOnClickListener {
            if (recordExist == true) {
                if (now == 0) {

                    testScoreList.clear()

                    uploadFile(0) // test score GET

                    // 백에 testIdList[0], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                    question.text = testList[1]
                    now++

                }
                else if (now == 1) {

                    uploadFile(1)

                    // 백에 testIdList[1], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                    question.text = testList[2]
                    now++

                }
                else if (now == 2) {

                    uploadFile(2)

                    // 백에 testIdList[2], wav 보내고 점수 저장 / 이걸 성공시 다음 실행
                    // 점수 다 더해줌 계산
                    total = testScoreList[0] + testScoreList[1] + testScoreList[2]
                    if (total >= 60.0) {
                        stage++
                        done = false
                        // ai 돌리고 점수 저장
                    }
                    else {
                        stage++
                        done = true
                    }
                    val intent = Intent(this@OnBoarding2_1Activity, OnBoarding2_1FinishActivity::class.java)
                    App.prefs.setStage(stage-1) // stage 저장
                    // 각 단어 전달
                    intent.putExtra("test0", testList[0])
                    intent.putExtra("test1", testList[1])
                    intent.putExtra("test2", testList[2])
                    // 각각의 점수도 전달 합니다잉
                    intent.putExtra("testScore0", testScoreList[0].toString())
                    intent.putExtra("testScore1", testScoreList[1].toString())
                    intent.putExtra("testScore2", testScoreList[2].toString())
                    intent.putExtra("done", done)
                    Log.e("done intent", done.toString())
                    startActivity(intent)

                    now = 0 // 여기서 저장해도 남아있나..? 아니면 객체 하나 만들어서 저장해주자 레벨도 같이 저장해도 될듯?

                }
            }
            else {
                Toast.makeText(this@OnBoarding2_1Activity, "Please record it!", Toast.LENGTH_SHORT).show()
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
        val file = File(fileName)

        val requestBody = RequestBody.create("audio/wav".toMediaTypeOrNull(), file)
        val fileToUpload = MultipartBody.Part.createFormData("multipartFile", file.name, requestBody)
        val filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)

        getScoreService.getScore(testIdList[i], fileToUpload).enqueue(object : Callback<BaseResponse<TestScoreDto>> {
            override fun onResponse(call: Call<BaseResponse<TestScoreDto>>, response: Response<BaseResponse<TestScoreDto>>) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("response.body()", response.body().toString())
                        Toast.makeText(applicationContext, response.message(), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    response.body()?.let { Log.v("Response", it.toString()) }
                }
            }

            override fun onFailure(call: Call<BaseResponse<TestScoreDto>>, t: Throwable) {
                t.message?.let { Log.e("error", it) } ?: "서버통신 실패(응답값 x)"
            }
        })
    }
    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}