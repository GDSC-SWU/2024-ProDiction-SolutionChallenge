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

    // view model
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // ViewModel 생성함수를 편하게 사용하고 싶다면?
    implementation ("androidx.fragment:fragment-ktx:1.5.3")
    implementation ("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.benchmark:benchmark-common:1.2.2")

    // 서버 통신
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))

    // define any required OkHttp artifacts without version (BOM을 위에 명시했으니, 밑의 버전들은 BOM에 맞게 적용됩니다.)
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}