package com.example.pro_diction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.MyPageDto
import com.example.pro_diction.data.dto.ResponseDto
import com.example.pro_diction.databinding.FragmentMyBinding
import com.example.pro_diction.presentation.auth.MainLoginActivity
import com.example.pro_diction.presentation.my.AgeActivity
import com.example.pro_diction.presentation.my.CallActivity
import com.example.pro_diction.presentation.my.LanguageActivity
import com.example.pro_diction.presentation.my.MyWordActivity
import com.example.pro_diction.presentation.onboarding.OnBoarding1Activity
import com.example.pro_diction.presentation.onboarding.OnBoarding2Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var nick = ""

    private lateinit var binding: FragmentMyBinding
    var googleSignInClient: GoogleSignInClient?= null
    var getMyPage = ApiPool.getMyPage

    lateinit var myInflater : View
    lateinit var ageIntent: Intent

    var logout = ApiPool.logout
    var profile = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // 구글 로그아웃 버튼 ID 가져오기
        var googleSignOutButton = view?.findViewById<Button>(R.id.btn_logout)

        // 구글 로그아웃을 위해 로그인 세션 가져오기
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1016660677235-epa63ipq5lp6ppeiih7vkpuug8pdjho4.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val account: GoogleSignInAccount? = this.context?.let {
            GoogleSignIn.getLastSignedInAccount(
                it
            )
        }
        nick = account?.displayName.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myInflater = inflater.inflate(R.layout.fragment_my, container, false)

        // 닉네임 nick name
        //myInflater.findViewById<TextView>(R.id.tv_nick).text = nick

        var tvLevel = myInflater.findViewById<TextView>(R.id.tv_my_level)
        // level
        if (App.prefs.getStage() == 1) {
            tvLevel.text = getString(R.string.learn_1)
        }
        else if (App.prefs.getStage() == 2) {
            tvLevel.text = getString(R.string.learn_2)
        }
        else if (App.prefs.getStage() == 3) {
            tvLevel.text = getString(R.string.learn_3)
        }
        else if (App.prefs.getStage() == 4) {
            tvLevel.text = getString(R.string.learn_4)
        }
        else if (App.prefs.getStage() == 5) {
            tvLevel.text = getString(R.string.learn_5)
        }
        else{
            tvLevel.text = "-"
        }

        // 로그아웃 버튼 logout button
        myInflater.findViewById<TextView>(R.id.tv_logout).setOnClickListener {
            logout.logout().enqueue(object: Callback<ResponseDto> {
                override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                    if (response.isSuccessful) {
                        if(response.body() != null) {
                            if (response.body()!!.code == 200)
                            {
                                Log.e("logout", response.body().toString())
                                signOut()
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                    Log.e("error", t.toString())
                }
            })

        }

        // 단어장 word list button
        val intent = Intent(this.context, MyWordActivity::class.java)
        myInflater.findViewById<ImageView>(R.id.btn_my_1).setOnClickListener {
            intent.putExtra("type", "1")
            startActivity(intent)
        }
        myInflater.findViewById<ImageView>(R.id.btn_my_2).setOnClickListener {
            intent.putExtra("type", "2")
            startActivity(intent)
        }
        myInflater.findViewById<ImageView>(R.id.btn_my_3).setOnClickListener {
            intent.putExtra("type", "3")
            startActivity(intent)
        }
        myInflater.findViewById<ImageView>(R.id.btn_my_4).setOnClickListener {
            intent.putExtra("type", "4")
            startActivity(intent)
        }
        myInflater.findViewById<ImageView>(R.id.btn_my_5).setOnClickListener {
            intent.putExtra("type", "5")
            startActivity(intent)
        }

        val callIntent = Intent(this.context, CallActivity::class.java)
        myInflater.findViewById<TextView>(R.id.tv_center).setOnClickListener {
            startActivity(callIntent)
        }
        myInflater.findViewById<ImageView>(R.id.iv_center).setOnClickListener {
            startActivity(callIntent)
        }

        val languageIntent = Intent(this.context, LanguageActivity::class.java)
        myInflater.findViewById<TextView>(R.id.tv_setting).setOnClickListener {
            startActivity(languageIntent)
        }
        myInflater.findViewById<ImageView>(R.id.iv_setting).setOnClickListener {
            startActivity(languageIntent)
        }

        // retest
        val retestIntent = Intent(this.context, OnBoarding2Activity::class.java)
        myInflater.findViewById<TextView>(R.id.tv_retest).setOnClickListener {
            startActivity(retestIntent)
        }


        // test 온보딩
        val onIntent = Intent(this.context, OnBoarding1Activity::class.java)
        myInflater.findViewById<Button>(R.id.btn_t).setOnClickListener {
            startActivity(onIntent)
        }

        // test stt
        val sttIntent = Intent(this.context, SttActivity::class.java)
        myInflater.findViewById<Button>(R.id.btn_t_stt).setOnClickListener {
            startActivity(sttIntent)
        }

        // record test
        val recordIntent = Intent(this.context, AudioRecordActivity::class.java)
        myInflater.findViewById<Button>(R.id.btn_t_record).setOnClickListener {
            startActivity(recordIntent)
        }

        // record
        val recordTIntent = Intent(this.context, MainActivity2::class.java)
        myInflater.findViewById<Button>(R.id.btn_t_record_2).setOnClickListener {
            startActivity(recordTIntent)
        }

        ageIntent = Intent(this.context, AgeActivity::class.java)
        refreshFragment()

        myInflater.findViewById<TextView>(R.id.tv_personal).setOnClickListener {
            startActivity(ageIntent)
        }
        return myInflater
    }

    fun refreshFragment() {
        getMyPage.getMypage().enqueue(object: Callback<BaseResponse<MyPageDto>> {
            override fun onResponse(
                call: Call<BaseResponse<MyPageDto>>,
                response: Response<BaseResponse<MyPageDto>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        myInflater.findViewById<TextView>(R.id.tv_nick).text =
                            response.body()!!.data?.nickname ?: ""
                        val url = URL(response.body()!!.data?.googleProfile ?: "")
                        val defaultImage = R.drawable.ic_default
                        val view = myInflater.findViewById<ImageView>(R.id.v_profile)
                        Glide.with(this@MyFragment)
                            .load(url)
                            .placeholder(defaultImage)
                            .error(defaultImage)
                            .fallback(defaultImage)
                            .circleCrop()
                            .into(view)
                        if (response.body()!!.data?.age != null) {
                            ageIntent.putExtra("age", response.body()!!.data?.age.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<MyPageDto>>, t: Throwable) {
                Log.e("error", t.toString())
            }
        })
    }
    override fun onResume() {
        super.onResume()
        // 프래그먼트를 재로딩하는 작업 수행
        refreshFragment()
        //Locale.setDefault(this.getSharedPreferences("Settings", Context.MODE_PRIVATE))
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            MyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun signOut() {
        googleSignInClient?.signOut()?.addOnCompleteListener(requireActivity()) {
            val logoutIntent = Intent(requireContext(), MainLoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(logoutIntent)
            Log.e("logout", "로그아웃")
            App.prefs.signOut()
        }
    }
}