plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id ("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"

    // Google services 플러그인
    //id("com.google.gms.google-services")
}


android {
    namespace = "com.example.pro_diction"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pro_diction"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    // 뷰 바인딩 사용
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Google Play services
    implementation ("com.google.gms:google-services:4.3.15")
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.firebase:firebase-bom:32.0.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    // firebase BoM
    //implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    //implementation("com.google.firebase:firebase-analytics")

    // Retrofit 의존성 추가
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")

    // googleid
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    // Matrial Design
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // ViewModel 생성함수를 편하게 사용하고 싶다면?
    implementation ("androidx.fragment:fragment-ktx:1.5.3")
    implementation ("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.benchmark:benchmark-common:1.2.2")

    // 서버 통신
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // define a BOM and its version
    //implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
    implementation("com.squareup.okhttp3:okhttp-bom:4.9.0")

    //kotlinX
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    implementation ("com.jakewharton.timber:timber:5.0.1")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation ("io.coil-kt:coil:2.3.0")

    // viewpager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // indicator
    implementation("me.relex:circleindicator:2.1.6")

    // define any required OkHttp artifacts without version (BOM을 위에 명시했으니, 밑의 버전들은 BOM에 맞게 적용됩니다.)
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")


    // mediapipe
    // implementation ("com.google.mediapipe:solution-core:0.10.9")
    // implementation ("com.google.mediapipe:hands:0.10.9")

    // permission
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    // Tensorflow
    implementation ("org.tensorflow:tensorflow-lite:2.10.0")
    //implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation ("org.tensorflow:tensorflow-lite-task-vision-play-services:0.4.2")
    implementation ("com.google.android.gms:play-services-tflite-gpu:16.0.0")
    implementation ("org.tensorflow:tensorflow-lite-task-vision:0.4.0")



    //
    implementation ("androidx.fragment:fragment-ktx:1.5.4")

    // Navigation library
    val nav_version = "2.5.3"
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    // CameraX core library
    val camerax_version = "1.2.0-alpha02"
    implementation ("androidx.camera:camera-core:$camerax_version")

    // CameraX Camera2 extensions
    implementation ("androidx.camera:camera-camera2:$camerax_version")

    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")

    // CameraX View class
    implementation ("androidx.camera:camera-view:$camerax_version")

    // WindowManager
    implementation ("androidx.window:window:1.1.0-alpha03")

    // Unit testing
    testImplementation ("junit:junit:4.13.2")

    // Instrumented testing
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    // Mediapipe Library
    implementation ("com.google.mediapipe:tasks-vision:0.10.0") // 0.10.0

    // ExoPlayer
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.19.1")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    // wav recorder
    implementation("com.github.squti:Android-Wave-Recorder:1.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}