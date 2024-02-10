package com.example.pro_diction.presentation.my

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.MyWordDto
import com.example.pro_diction.data.dto.SentenseDetailDto
import com.example.pro_diction.data.dto.SentenseDto
import com.example.pro_diction.databinding.MywordItemBinding
import com.example.pro_diction.databinding.SentenseItemBinding
import com.example.pro_diction.presentation.learn.sentense.SentenseAdapter

class MyWordAdapter (private val dataList: MutableList<MyWordDto>) :
    RecyclerView.Adapter<MyWordAdapter.MyWordViewHolder>() {
    private lateinit var binding: MywordItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWordViewHolder {
        binding = MywordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyWordViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class MyWordViewHolder(val binding: MywordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var mywordBtn : Button = binding.btnMyword

        init {
            binding.btnMyword.setOnClickListener{
                Log.e("btnMyword", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnMyword, pos)
                }
            }
        }
        fun bind(myword: MyWordDto) {
            mywordBtn.text = myword.item
        }
    }

    override fun getItemCount() = dataList.size
}