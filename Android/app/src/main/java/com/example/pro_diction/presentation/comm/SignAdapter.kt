package com.example.pro_diction.presentation.comm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.SignDto
import com.example.pro_diction.data.dto.WordListDto
import com.example.pro_diction.databinding.MywordItemBinding
import com.example.pro_diction.databinding.SignItemBinding
import com.example.pro_diction.presentation.my.MyWordAdapter

class SignAdapter (private val dataList: MutableList<SignDto>) :
    RecyclerView.Adapter<SignAdapter.SignViewHolder>() {
    private lateinit var binding: SignItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignViewHolder {
        binding = SignItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SignViewHolder(val binding: SignItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var ivHand : ImageView = binding.ivHand
        var tvHand : TextView = binding.tvHand

        fun bind(sign: SignDto) {
            tvHand.text = sign.name
            ivHand.setImageDrawable(binding.root.context.getDrawable(sign.image))
        }
    }

    override fun getItemCount() = dataList.size
}