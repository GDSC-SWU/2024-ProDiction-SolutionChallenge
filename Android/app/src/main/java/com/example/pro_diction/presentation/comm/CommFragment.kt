/*
 * Copyright 2022 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pro_diction.presentation.comm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pro_diction.R
import com.example.pro_diction.data.AiApiPool
import com.example.pro_diction.databinding.FragmentCommBinding
import com.google.android.play.integrity.internal.t
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CommFragment : Fragment(),
    GestureRecognizerHelper.GestureRecognizerListener {

    companion object {
        private const val TAG = "Hand gesture recognizer"
    }

    private var _fragmentCommBinding: FragmentCommBinding? = null

    private val fragmentCommBinding
        get() = _fragmentCommBinding!!

    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    private val viewModel: MainViewModel by activityViewModels()
    private var defaultNumResults = 1
    private val gestureRecognizerResultAdapter: GestureRecognizerResultsAdapter by lazy {
        GestureRecognizerResultsAdapter().apply {
            updateAdapterSize(defaultNumResults)
        }
    }
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraFacing = CameraSelector.LENS_FACING_FRONT

    /** Blocking ML operations are performed using this executor */
    private lateinit var backgroundExecutor: ExecutorService

    // permissions
    private val REQUIRED_PERMISSIONS = mutableListOf(android.Manifest.permission.INTERNET,
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.ACCESS_NETWORK_STATE, android.Manifest.permission.RECORD_AUDIO).toTypedArray()

    // stt
    lateinit var speechRecognizer: SpeechRecognizer

    // tts
    private lateinit var tts: TextToSpeech
    var isFirst = true

    // ai/ml
    var recogList: MutableList<String> = mutableListOf()
    var lastStr = ""
    var textList: MutableList<String> = mutableListOf()

    // ai api
    var joinJamos = AiApiPool.joinJamos
    var splitJamos = AiApiPool.splitJamos

    fun getKoreanLetter(str: String): String {
        val koreanMap = mapOf(
            "giyeok" to "ㄱ",
            "nieun" to "ㄴ",
            "digeut" to "ㄷ",
            "rieul" to "ㄹ",
            "mieum" to "ㅁ",
            "bieup" to "ㅂ",
            "siot" to "ㅅ",
            "ieung" to "ㅇ",
            "jieut" to "ㅈ",
            "chieut" to "ㅊ",
            "kieuk" to "ㅋ",
            "tieut" to "ㅌ",
            "pieup" to "ㅍ",
            "hieut" to "ㅎ",
            "a" to "ㅏ",
            "ya" to "ㅑ",
            "eo" to "ㅓ",
            "yeo" to "ㅕ",
            "o" to "ㅗ",
            "yo" to "ㅛ",
            "u" to "ㅜ",
            "yu" to "ㅠ",
            "eu" to "ㅡ",
            "i" to "ㅣ",
            "ae" to "ㅐ",
            "yae" to "ㅒ",
            "e" to "ㅔ",
            "ye" to "ㅖ",
            "oe" to "ㅚ",
            "wi" to "ㅟ",
            "ui" to "ㅢ",
            "add" to "add",
            "space" to "space",
            "clear_one" to "clear_one",
            "clear_all" to "clear_all"
        )

        return koreanMap[str] ?: ""
    }

    fun addJa (s1: String, s2: String) : String {
        return when {
            s1 == "ㄱ" && s2 == "ㄱ" -> "ㄲ"
            s1 == "ㄷ" && s2 == "ㄷ" -> "ㄸ"
            s1 == "ㅂ" && s2 == "ㅂ" -> "ㅃ"
            s1 == "ㅅ" && s2 == "ㅅ" -> "ㅆ"
            s1 == "ㅈ" && s2 == "ㅈ" -> "ㅉ"
            s1 == "ㅅ" && s2 == "ㄱ" -> "ㄳ"
            s1 == "ㅈ" && s2 == "ㄴ" -> "ㄵ"
            s1 == "ㅎ" && s2 == "ㄴ" -> "ㄶ"
            s1 == "ㄱ" && s2 == "ㄹ" -> "ㄺ"
            s1 == "ㅁ" && s2 == "ㄹ" -> "ㄻ"
            s1 == "ㅂ" && s2 == "ㄹ" -> "ㄼ"
            s1 == "ㅅ" && s2 == "ㄹ" -> "ㄽ"
            s1 == "ㅌ" && s2 == "ㄹ" -> "ㄾ"
            s1 == "ㅍ" && s2 == "ㄹ" -> "ㄿ"
            s1 == "ㅎ" && s2 == "ㄹ" -> "ㅀ"
            s1 == "ㅅ" && s2 == "ㅂ" -> "ㅄ"
            s1 == "ㅏ" && s2 == "ㅗ" -> "ㅘ"
            s1 == "ㅐ" && s2 == "ㅗ" -> "ㅙ"
            s1 == "ㅓ" && s2 == "ㅜ" -> "ㅝ"
            s1 == "ㅔ" && s2 == "ㅜ" -> "ㅞ"
            else -> " " // 처리하지 않은 경우 빈 문자 반환
        }
    }
    var recodeStr: String = ""
        set(value) {
            field = value
            // TextView에 변경된 recodeStr 값을 설정
            fragmentCommBinding.tvCommStt.text = value
        }

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
        /*
        if (!PermissionsFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(
                requireActivity(), R.id.main_container
            ).navigate(R.id.action_comm_to_permissions)
        }*/

        // Start the GestureRecognizerHelper again when users come back
        // to the foreground.
        backgroundExecutor.execute {
            if (gestureRecognizerHelper.isClosed()) {
                gestureRecognizerHelper.setupGestureRecognizer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::gestureRecognizerHelper.isInitialized) {
            viewModel.setMinHandDetectionConfidence(gestureRecognizerHelper.minHandDetectionConfidence)
            viewModel.setMinHandTrackingConfidence(gestureRecognizerHelper.minHandTrackingConfidence)
            viewModel.setMinHandPresenceConfidence(gestureRecognizerHelper.minHandPresenceConfidence)
            viewModel.setDelegate(gestureRecognizerHelper.currentDelegate)

            // Close the Gesture Recognizer helper and release resources
            backgroundExecutor.execute { gestureRecognizerHelper.clearGestureRecognizer() }
        }
    }

    override fun onDestroyView() {
        _fragmentCommBinding = null
        super.onDestroyView()

        // Shut down our background executor
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCommBinding =
            FragmentCommBinding.inflate(inflater, container, false)

        val rootLayout = fragmentCommBinding.root
        fragmentCommBinding.editCommTts.visibility = View.VISIBLE

        // 화면 클릭 시 키보드 숨기기
        rootLayout.setOnClickListener {
            hideKeyboard()
        }

        // permission
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                //setupStreamingModePipeline()

                //glSurfaceView.post { startCamera() }
                //glSurfaceView.visibility = View.VISIBLE

                //initView()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한을 다시 설정해주세요!", Toast.LENGTH_SHORT).show()
            }

        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(*REQUIRED_PERMISSIONS)
            .check()

        // stt
        fragmentCommBinding.btnCommStt.setOnClickListener {
            startVoiceRecording()
            fragmentCommBinding.tvCommStt.text = recodeStr
        }
        fragmentCommBinding.tvCommStt.text = recodeStr

        // tts
        initTTS()
        // tts 버튼 & edit text
        fragmentCommBinding.btnCommTts.setOnClickListener {
            speakOut(fragmentCommBinding.editCommTts.text.toString())
            // tts 버튼 말한 후 edit text 초기화
            fragmentCommBinding.editCommTts.text.clear()
        }

        // keyboard 버튼
        fragmentCommBinding.btnCommKeyboard.setOnClickListener {
            fragmentCommBinding.editCommTts.visibility = View.VISIBLE
            // EditText에 포커스를 설정하여 키보드가 나타나도록 함
            fragmentCommBinding.editCommTts.requestFocus()
            showKeyboard()
        }

        fragmentCommBinding.editCommTts.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                fragmentCommBinding.editCommTts.visibility = View.VISIBLE

                splitJamos.splitJamos(fragmentCommBinding.editCommTts.text.toString()).enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body() != "") {
                                /*for (char in response.body().toString()) {
                                    textList.add(char.toString())
                                }*/
                                if (response.body().toString() != textList.joinToString(separator = "")) {
                                    textList.clear()
                                    for (char in response.body().toString()) {
                                        textList.add(char.toString())
                                    }
                                }
                            }


                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("error", t.toString())
                    }
                })
            }
        })

        // textList에 변경이 일어난 경우 textList의 글자를 string으로 바꾸고 api로 전달해서 받아온 다음에 edittext화면에 넣음
        // edittext에 클릭 발생한 경우 textList 비우기 + 에디트 텍스트 비우기. 그 다음에 testList가 비어있으면 클릭해도 에디트 텍스트 지우지 말기. 
        // 키보드 버튼도 마찬가지
        return rootLayout
    }

    // 키보드 숨기기
    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    // 키보드 보이기
    private fun showKeyboard() {
        // 키보드 매니저 가져오기
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 키보드 나타내기
        inputMethodManager.showSoftInput(fragmentCommBinding.editCommTts, InputMethodManager.SHOW_IMPLICIT)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fragmentCommBinding.recyclerviewResults) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gestureRecognizerResultAdapter
        }

        // Initialize our background executor
        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Wait for the views to be properly laid out
        fragmentCommBinding.viewFinder.post {
            // Set up the camera and its use cases
            setUpCamera()
        }

        // Create the Hand Gesture Recognition Helper that will handle the
        // inference
        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                minHandDetectionConfidence = viewModel.currentMinHandDetectionConfidence,
                minHandTrackingConfidence = viewModel.currentMinHandTrackingConfidence,
                minHandPresenceConfidence = viewModel.currentMinHandPresenceConfidence,
                currentDelegate = viewModel.currentDelegate,
                gestureRecognizerListener = this
            )
        }

        // Attach listeners to UI control widgets
        initBottomSheetControls()
    }

    private fun initBottomSheetControls() {
        // init bottom sheet settings
        fragmentCommBinding.bottomSheetLayout.detectionThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinHandDetectionConfidence
            )
        fragmentCommBinding.bottomSheetLayout.trackingThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinHandTrackingConfidence
            )
        fragmentCommBinding.bottomSheetLayout.presenceThresholdValue.text =
            String.format(
                Locale.US, "%.2f", viewModel.currentMinHandPresenceConfidence
            )

        // When clicked, lower hand detection score threshold floor
        fragmentCommBinding.bottomSheetLayout.detectionThresholdMinus.setOnClickListener {
            if (gestureRecognizerHelper.minHandDetectionConfidence >= 0.2) {
                gestureRecognizerHelper.minHandDetectionConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise hand detection score threshold floor
        fragmentCommBinding.bottomSheetLayout.detectionThresholdPlus.setOnClickListener {
            if (gestureRecognizerHelper.minHandDetectionConfidence <= 0.8) {
                gestureRecognizerHelper.minHandDetectionConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, lower hand tracking score threshold floor
        fragmentCommBinding.bottomSheetLayout.trackingThresholdMinus.setOnClickListener {
            if (gestureRecognizerHelper.minHandTrackingConfidence >= 0.2) {
                gestureRecognizerHelper.minHandTrackingConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise hand tracking score threshold floor
        fragmentCommBinding.bottomSheetLayout.trackingThresholdPlus.setOnClickListener {
            if (gestureRecognizerHelper.minHandTrackingConfidence <= 0.8) {
                gestureRecognizerHelper.minHandTrackingConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, lower hand presence score threshold floor
        fragmentCommBinding.bottomSheetLayout.presenceThresholdMinus.setOnClickListener {
            if (gestureRecognizerHelper.minHandPresenceConfidence >= 0.2) {
                gestureRecognizerHelper.minHandPresenceConfidence -= 0.1f
                updateControlsUi()
            }
        }

        // When clicked, raise hand presence score threshold floor
        fragmentCommBinding.bottomSheetLayout.presenceThresholdPlus.setOnClickListener {
            if (gestureRecognizerHelper.minHandPresenceConfidence <= 0.8) {
                gestureRecognizerHelper.minHandPresenceConfidence += 0.1f
                updateControlsUi()
            }
        }

        // When clicked, change the underlying hardware used for inference.
        // Current options are CPU and GPU
        fragmentCommBinding.bottomSheetLayout.spinnerDelegate.setSelection(
            viewModel.currentDelegate, false
        )
        fragmentCommBinding.bottomSheetLayout.spinnerDelegate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long
                ) {
                    try {
                        gestureRecognizerHelper.currentDelegate = p2
                        updateControlsUi()
                    } catch(e: UninitializedPropertyAccessException) {
                        Log.e(TAG, "GestureRecognizerHelper has not been initialized yet.")

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    /* no op */
                }
            }
    }

    // Update the values displayed in the bottom sheet. Reset recognition
    // helper.
    private fun updateControlsUi() {
        fragmentCommBinding.bottomSheetLayout.detectionThresholdValue.text =
            String.format(
                Locale.US,
                "%.2f",
                gestureRecognizerHelper.minHandDetectionConfidence
            )
        fragmentCommBinding.bottomSheetLayout.trackingThresholdValue.text =
            String.format(
                Locale.US,
                "%.2f",
                gestureRecognizerHelper.minHandTrackingConfidence
            )
        fragmentCommBinding.bottomSheetLayout.presenceThresholdValue.text =
            String.format(
                Locale.US,
                "%.2f",
                gestureRecognizerHelper.minHandPresenceConfidence
            )

        // Needs to be cleared instead of reinitialized because the GPU
        // delegate needs to be initialized on the thread using it when applicable
        backgroundExecutor.execute {
            gestureRecognizerHelper.clearGestureRecognizer()
            gestureRecognizerHelper.setupGestureRecognizer()
        }
        fragmentCommBinding.overlay.clear()
    }

    // Initialize CameraX, and prepare to bind the camera use cases
    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(requireContext())
        )
    }

    // Declare and bind preview, capture and analysis use cases
    @SuppressLint("UnsafeOptInUsageError")
    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(cameraFacing).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(fragmentCommBinding.viewFinder.display.rotation)
            .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(fragmentCommBinding.viewFinder.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(backgroundExecutor) { image ->
                        recognizeHand(image)
                    }
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(fragmentCommBinding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun recognizeHand(imageProxy: ImageProxy) {
        gestureRecognizerHelper.recognizeLiveStream(
            imageProxy = imageProxy,
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        imageAnalyzer?.targetRotation =
            fragmentCommBinding.viewFinder.display.rotation
    }

    // Update UI after a hand gesture has been recognized. Extracts original
    // image height/width to scale and place the landmarks properly through
    // OverlayView. Only one result is expected at a time. If two or more
    // hands are seen in the camera frame, only one will be processed.
    override fun onResults(
        resultBundle: GestureRecognizerHelper.ResultBundle
    ) {
        activity?.runOnUiThread {
            if (_fragmentCommBinding != null) {
                /*
                val alreadyText = fragmentCommBinding.editCommTts.text
                if (alreadyText != null && alreadyText.toString() != "") {
                    splitJamos.splitJamos(alreadyText.toString()).enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {
                                if (response.body() != null && response.body() != "") {
                                    for (char in response.body().toString()) {
                                        textList.add(char.toString())
                                    }
                                }


                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.e("error", t.toString())
                        }
                    })
                }*/

                // Show result of recognized gesture
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    gestureRecognizerResultAdapter.updateResults(
                        gestureCategories.first()
                    )
                    val firstGesture = gestureCategories.first()
                    if (gestureCategories.first()?.first()?.categoryName() != null && gestureCategories.first()?.first()?.categoryName() != "") {
                        val category = gestureCategories.first()?.first()?.categoryName()?: null
                        Log.e("category", category.toString())
                        val str = getKoreanLetter(category.toString())
                        Log.e("str", str)


                        if (recogList.size == 20) {
                            Log.e("recog", recogList[0])
                            if (lastStr == str) {
                                Log.e("recogList if (lastStr == str)", recogList.toString())
                                lastStr = ""

                                if (recogList[0] == "clear_all") {
                                    if (textList != null) {
                                        textList.clear()
                                        fragmentCommBinding.editCommTts.setText("")
                                    } else {

                                    }
                                } else if (recogList[0] == "add") {
                                    Log.e("textList.size", textList.size.toString())
                                    if (textList.size >= 2) {
                                        var ja = addJa(
                                            textList[textList.size - 1],
                                            textList[textList.size - 2]
                                        )
                                        Log.e("addJa", ja)
                                        Log.e("textList[textList.size - 2]", textList[textList.size - 2])
                                        Log.e("textList[textList.size - 1]", textList[textList.size - 1])
                                        if (ja != " ") {

                                            textList.removeAt(textList.size - 1)
                                            Log.e("textList[textList.size - 2]", textList[textList.size - 1])
                                            textList.removeAt(textList.size - 1)
                                            Log.e("textList[textList.size - 1]", textList[textList.size - 1])

                                            textList.add(ja)
                                            joinJamos(textList)
                                        }

                                    } else {

                                    }
                                } else if (recogList[0] == "clear_one") {
                                    if (textList != null && textList.size > 0) {
                                        Log.e("if clear_one0", textList.size.toString())
                                        textList.removeAt(textList.size - 1)
                                        Log.e("if clear_one1", textList.size.toString())
                                        joinJamos(textList)
                                        Log.e("if clear_one2", textList.toString())
                                    } else {

                                    }
                                } else if (recogList[0] == "space") {
                                    textList.add(" ")
                                    joinJamos(textList)
                                } else {
                                    Log.e("textList", textList.toString())
                                    textList.add(recogList[0])
                                    Log.e("textList", textList.toString())
                                    Log.e("recogList[0]", recogList[0].toString())
                                    Log.e("textList", textList.toString())
                                    joinJamos(textList)
                                    Log.e("textList", textList.toString())
                                    recogList.clear()
                                }
                            }
                            else {
                                if (recogList.size == 0) {
                                    recogList.add(str)
                                    //Log.e("recogList2", recogList.toString())
                                    lastStr = str
                                }else {
                                    recogList.clear()
                                    recogList.add(str)
                                    //Log.e("recogList3", recogList.toString())
                                    lastStr = str
                                }

                            }
                            recogList.clear()
                        }
                        else {
                            if (lastStr == str) {
                                recogList.add(str)
                                Log.e("recogList1", recogList.toString())
                                Log.e("textList1", textList.toString())
                                lastStr = str
                            }
                            else {
                                if (recogList.size == 0) {
                                    recogList.add(str)
                                    Log.e("recogList2", recogList.toString())
                                    lastStr = str
                                }
                                else {
                                    recogList.clear()
                                    recogList.add(str)
                                    Log.e("recogList3", recogList.toString())
                                    Log.e("textList3", textList.toString())
                                    lastStr = str
                                }

                            }
                        }
                    }

                } else {
                    gestureRecognizerResultAdapter.updateResults(emptyList())
                }

                fragmentCommBinding.bottomSheetLayout.inferenceTimeVal.text =
                    String.format("%d ms", resultBundle.inferenceTime)

                // Pass necessary information to OverlayView for drawing on the canvas
                fragmentCommBinding.overlay.setResults(
                    resultBundle.results.first(),
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM
                )

                // Force a redraw
                fragmentCommBinding.overlay.invalidate()
            }
        }
    }
    fun joinJamos(list: MutableList<String>) {
        var text = list.joinToString(separator = "")
        joinJamos.joinJamos(text).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    fragmentCommBinding.editCommTts.clearFocus()
                    fragmentCommBinding.editCommTts.setText(response.body().toString())
                } else {
                    Log.e("joinJamos", response.toString())
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("joinJamos", t.toString())
            }
        })
    }
    override fun onError(error: String, errorCode: Int) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            gestureRecognizerResultAdapter.updateResults(emptyList())

            if (errorCode == GestureRecognizerHelper.GPU_ERROR) {
                fragmentCommBinding.bottomSheetLayout.spinnerDelegate.setSelection(
                    GestureRecognizerHelper.DELEGATE_CPU, false
                )
            }
        }
    }
    // android stt SpeechRecognizer
    private fun startVoiceRecording() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity()?.getPackageName())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(intent)
    }

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(requireContext(), "음성인식 시작", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    // android tts TextToSpeech
    private fun initTTS() {
        tts = TextToSpeech(requireContext(), textToSpeechListener)
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
        Toast.makeText(requireContext(), "재생 중..", Toast.LENGTH_SHORT).show()
    }

}
