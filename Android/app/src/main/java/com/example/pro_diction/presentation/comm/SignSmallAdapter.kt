package com.example.pro_diction.presentation.comm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.SignDto
import com.example.pro_diction.databinding.SignItemBinding
import com.example.pro_diction.databinding.SignSmallItemBinding

class SignSmallAdapter (private val dataList: MutableList<SignDto>) :
    RecyclerView.Adapter<SignSmallAdapter.SignSmallViewHolder>() {
    private lateinit var binding: SignSmallItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignSmallViewHolder {
        binding = SignSmallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignSmallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SignSmallViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SignSmallViewHolder(val binding: SignSmallItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var ivHand: ImageView = binding.ivHand
        var tvHand: TextView = binding.tvHand

        fun bind(sign: SignDto) {
            tvHand.text = sign.name
            ivHand.setImageDrawable(binding.root.context.getDrawable(sign.image))
        }
    }

    override fun getItemCount() = dataList.size
}