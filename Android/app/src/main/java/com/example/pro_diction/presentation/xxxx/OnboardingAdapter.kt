package com.example.pro_diction.presentation.xxxx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter (fa: FragmentActivity, private val count: Int) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        val index = getRealPosition(position)

        return when (index) {
            0 -> OnboardingFragment_1()
            //1 -> OnboardingFragment_2()
            else -> OnboardingFragment_3()
        }
    }

    override fun getItemCount(): Int {
        return 2000
    }

    private fun getRealPosition(position: Int): Int {
        return position % count
    }
}