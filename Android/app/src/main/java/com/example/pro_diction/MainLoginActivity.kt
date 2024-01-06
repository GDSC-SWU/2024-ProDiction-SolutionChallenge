package com.example.pro_diction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Dimension
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainLoginActivity : AppCompatActivity() {

    // GoogleSignInClient는 MainLoginActivity에서 관리
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleAuthLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)

                // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
                val userName: String = account?.givenName ?: ""
                val serverAuth: String = account?.serverAuthCode ?: ""

                // Google 로그인 성공 시의 추가 로직을 여기에 추가
                // MainActivity로 이동하는 코드 추가
                moveMainActivity(userName, serverAuth)

            } catch (e: ApiException) {
                Log.e(MainLoginActivity::class.java.simpleName, e.stackTraceToString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        // GoogleSignInClient 초기화
        googleSignInClient = getGoogleClient()

        // 클릭 이벤트 리스너 등록
        findViewById<View>(R.id.cl_google_login).setOnClickListener {
            requestGoogleLogin()
        }
    }

    private fun requestGoogleLogin() {
        // Google 로그인 시 signOut 호출
        googleSignInClient.signOut()

        // Google 로그인 인텐트 실행
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(getString(R.string.google_login_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun moveMainActivity(userName: String, serverAuthCode: String) {
        // 서버로 전송할 데이터 생성
        val userDTO = UserDTO(userName, serverAuthCode)

        // Retrofit 인터페이스 생성
        val userApiService = UserApiService.create()

        // Coroutine을 사용한 비동기 호출
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = userApiService.loginUser(userDTO)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // 서버 응답을 처리
                        val responseBody = response.body()
                        if (responseBody != null) {
                            // responseBody 사용 가능
                        } else {
                            // 서버 응답이 null인 경우 처리
                        }

                        // 다음 Activity로 이동하는 코드
                        val intent = Intent(this@MainLoginActivity, MainActivity::class.java)
                        startActivity(intent)

                        // 현재 Activity 종료
                        finish()
                    } else {
                        // 서버 에러 처리
                        // response.errorBody()를 통해 에러 내용 확인 가능
                        Log.e(MainLoginActivity::class.java.simpleName, "Server response failed: ${response.errorBody()}")

                    }
                }
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                Log.e(MainLoginActivity::class.java.simpleName, "Exception during network request: ${e.message}")
                // 에러 처리 로직 추가
            }
        }
    }


}