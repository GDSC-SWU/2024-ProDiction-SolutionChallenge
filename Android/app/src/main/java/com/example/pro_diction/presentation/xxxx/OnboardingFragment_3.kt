package com.example.pro_diction.presentation.xxxx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pro_diction.R

class OnboardingFragment_3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup = inflater.inflate(
            R.layout.activity_on_boarding3, container, false
        ) as ViewGroup

        return rootView
    }
}