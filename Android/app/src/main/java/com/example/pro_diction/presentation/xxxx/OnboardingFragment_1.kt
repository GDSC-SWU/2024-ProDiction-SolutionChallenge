package com.example.pro_diction.presentation.xxxx

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pro_diction.R

//import com.example.pro_diction.databinding.Onboarding1Binding

class OnboardingFragment_1 : Fragment() {

    //private lateinit var binding: Onboarding1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //binding = Onboarding1Binding.inflate(inflater)
        //return binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        binding.editOnboarding1.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
                // 텍스트 변경 전에 수행할 작업
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중에 수행할 작업
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후에 수행할 작업
                updateButtonState(s.toString(), binding.btnOnboarding1)
            }
        })*/
    }

    private fun updateButtonState(text: String, button: Button) {
        if (text.isNotEmpty()) {
            // EditText에 값이 있을 때 버튼의 색상과 텍스트 변경
            button.setBackgroundResource(R.drawable.bg_background_round_on)
            button.setTextColor(Color.parseColor("@color/sub3"))
            button.isClickable = true
        } else {
            // EditText에 값이 없을 때 버튼을 초기 상태로 변경
            button.setBackgroundResource(R.drawable.bg_background_round)
            button.setTextColor(Color.parseColor("#B9B9B9"))
            button.isClickable = false
        }
    }
}