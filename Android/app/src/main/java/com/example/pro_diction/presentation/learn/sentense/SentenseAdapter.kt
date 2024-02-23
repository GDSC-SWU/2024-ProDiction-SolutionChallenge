package com.example.pro_diction.presentation.learn.sentense

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.PhraseDto
import com.example.pro_diction.data.dto.SentenseDto
import com.example.pro_diction.databinding.PhraseItemBinding
import com.example.pro_diction.databinding.SentenseItemBinding
import com.example.pro_diction.presentation.learn.phrase.PhraseAdapter

class SentenseAdapter (private val dataList: MutableList<CategoryResponseDto>) :
    RecyclerView.Adapter<SentenseAdapter.SentenseViewHolder>() {
    private lateinit var binding: SentenseItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenseViewHolder {
        binding = SentenseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SentenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SentenseViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SentenseViewHolder(val binding: SentenseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var icSentense : TextView = binding.icSentense
        var tvSentenseMain : TextView = binding.tvSentenseMain
        var btnSentense1: Button = binding.btnSentense1
        var btnSentense2: Button = binding.btnSentense2
        var btnRight: ImageButton = binding.btnSentenseRight

        init {
            binding.icSentense.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSentenseRight, pos)
                }
            }
            binding.btnSentenseRight.setOnClickListener{
                Log.e("btnSentenseRight", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSentenseRight, pos)
                }
            }
            binding.tvSentenseMain.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.tvSentenseMain, pos)
                }
            }
            binding.btnSentense1.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSentense1, pos)
                }
            }
            binding.btnSentense2.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSentense2, pos)
                }
            }

        }
        fun bind(categoyResponse: CategoryResponseDto) {
            tvSentenseMain.text = categoyResponse.name
            btnSentense1.text = categoyResponse.studyResponseDtoList.get(0).content
            btnSentense2.text = categoyResponse.studyResponseDtoList.get(1).content

        }
    }

    override fun getItemCount() = dataList.size
}