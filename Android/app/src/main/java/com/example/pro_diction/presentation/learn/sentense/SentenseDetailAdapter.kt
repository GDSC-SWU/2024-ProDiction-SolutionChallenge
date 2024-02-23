package com.example.pro_diction.presentation.learn.sentense

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.PhraseDetailDto
import com.example.pro_diction.data.dto.SentenseDetailDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.databinding.PhraseDetailItemBinding
import com.example.pro_diction.databinding.SentenseDetailItemBinding
import com.example.pro_diction.presentation.learn.phrase.PhraseDetailAdapter

class SentenseDetailAdapter (private val dataList: MutableList<StudyResponseDto>) :
    RecyclerView.Adapter<SentenseDetailAdapter.SentenseDetailViewHolder>() {
    private lateinit var binding: SentenseDetailItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenseDetailViewHolder {
        binding = SentenseDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SentenseDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SentenseDetailViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SentenseDetailViewHolder(val binding: SentenseDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var sentenseDetailBtn : Button = binding.btnSentenseDetail

        init {
            binding.btnSentenseDetail.setOnClickListener{
                Log.e("btn", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSentenseDetail, pos)
                }
            }
        }
        fun bind(studyResponse: StudyResponseDto) {
            sentenseDetailBtn.text = studyResponse.content
        }
    }

    override fun getItemCount() = dataList.size
}