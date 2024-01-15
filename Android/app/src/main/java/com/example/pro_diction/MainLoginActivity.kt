package com.example.pro_diction

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat.startActivity
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.PasswordCredential
import androidx.databinding.DataBindingUtil.setContentView
import com.example.pro_diction.databinding.ActivityMainLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.pro_diction.UiState
import java.io.IOException

class MainLoginActivity : AppCompatActivity() {

    private lateinit var googleSignResultLauncher: ActivityResultLauncher<Intent>
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        getGoogleClient()
        googleSignResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
        observe()
    }

    private fun getGoogleClient() {
        findViewById<View>(R.id.cl_google_login).setOnClickListener {
            val googleSignInOption =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestServerAuthCode(getString(R.string.SERVER_CLIENT_ID))
                    .requestIdToken(getString(R.string.SERVER_CLIENT_ID))
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
        }
    }

    private fun observe() {
        viewModel.accessToken.observe(this) {
            when (it) {
                is UiState.Success -> {
                    navigateTo<MainActivity>()
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
