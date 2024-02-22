package com.example.pro_diction.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.pro_diction.App
import com.example.pro_diction.MainActivity
import com.example.pro_diction.presentation.onboarding.OnBoarding1Activity
import com.example.pro_diction.R
import com.example.pro_diction.coreui.view.UiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class MainLoginActivity : AppCompatActivity() {

    private lateinit var googleSignResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        getGoogleClient()

        googleSignResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            handleSignInResult(task)
        }
        observe()
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null && App.prefs.getLoggedInBefore() == true) { // account != null || App.prefs.getLoggedInBefore() == true
            // 이미 로그인 되어있으면 바로 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            Log.e("Logged", "${App.prefs.getLoggedIn()}")
        }
    }

    private fun getGoogleClient() {
        findViewById<View>(R.id.cl_google_login).setOnClickListener {
            val googleSignInOption =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestServerAuthCode("1016660677235-2t9s0516ga3u2intbuga7dvp0uaro0v6.apps.googleusercontent.com")
                    .requestIdToken("1016660677235-2t9s0516ga3u2intbuga7dvp0uaro0v6.apps.googleusercontent.com")
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)
            var signIntent: Intent = mGoogleSignInClient.signInIntent
            googleSignResultLauncher.launch(signIntent)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            var googleTokenAuth = account?.idToken.toString()
            Log.d("token", googleTokenAuth)
            if (!googleTokenAuth.isNullOrBlank()) {
                viewModel.postGoogleLogin(googleTokenAuth)
            }
        } catch (e: ApiException) {
            Log.d("ddd", "signInResult:failed Code = " + e.statusCode)
            e.printStackTrace()
        }
    }


    private fun observe() {
        viewModel.accessToken.observe(this) {
            when (it) {
                is UiState.Success -> {
                    if (App.prefs.getLoggedInBefore() == true) {
                        navigateTo<MainActivity>()
                    } else{
                        navigateTo<OnBoarding1Activity>()
                        App.prefs.setLoggedInBefore(true) // 첫 로그인 성공
                    }
                }
                else -> Unit
            }
        }
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this@MainLoginActivity, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}
