package com.example.pro_diction.presentation.onboarding

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.OnBoardingResultDto
import com.example.pro_diction.databinding.OnboardingResultItemBinding
import com.example.pro_diction.databinding.SyllableDetailItemBinding
import com.example.pro_diction.presentation.learn.syllable.SyllableDetailAdapter

class OnBoardingResultAdapter (private val dataList: MutableList<OnBoardingResultDto>) :
    RecyclerView.Adapter<OnBoardingResultAdapter.OnBoardingResultViewHolder>() {
    private lateinit var binding: OnboardingResultItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingResultViewHolder {
        binding = OnboardingResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingResultViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class OnBoardingResultViewHolder(val binding: OnboardingResultItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvQuestion : TextView = binding.tvOnboardingResult
        var progressBar = binding.progressBar
        var tvPercent : TextView = binding.tvOnboardingPercent


        fun bind(onboardingResult: OnBoardingResultDto) {
            tvQuestion.text = onboardingResult.question
            progressBar.progress = onboardingResult.progress?.toDouble()?.toInt() ?: 0
            tvPercent.text = onboardingResult.percent.toString() + "%"
            if (onboardingResult.percent!!.toDouble() >= 50.0) {
                tvQuestion.setTextColor(Color.parseColor("#2F4C74"))
                progressBar.progressTintList = ColorStateList.valueOf(Color.parseColor("#2F4C74"))
                tvPercent.setTextColor(Color.parseColor("#2F4C74"))

            }
            else {
                tvQuestion.setTextColor(Color.parseColor("#BC4141"))
                progressBar.progressTintList = ColorStateList.valueOf(Color.parseColor("#BC4141"))
                tvPercent.setTextColor(Color.parseColor("#BC4141"))
            }
        }
    }

    override fun getItemCount() = dataList.size
}