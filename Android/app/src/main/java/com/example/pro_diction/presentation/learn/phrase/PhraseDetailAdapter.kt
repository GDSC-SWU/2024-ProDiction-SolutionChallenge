package com.example.pro_diction.presentation.learn.phrase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.PhraseDetailDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.data.dto.WordDetailDto
import com.example.pro_diction.databinding.PhraseDetailItemBinding
import com.example.pro_diction.databinding.WordDetailItemBinding
import com.example.pro_diction.presentation.learn.word.WordDetailAdapter

class PhraseDetailAdapter (private val dataList: MutableList<StudyResponseDto>) :
    RecyclerView.Adapter<PhraseDetailAdapter.PhraseDetailViewHolder>() {
    private lateinit var binding: PhraseDetailItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhraseDetailViewHolder {
        binding = PhraseDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhraseDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhraseDetailViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class PhraseDetailViewHolder(val binding: PhraseDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var phraseDetailBtn : Button = binding.btnPhraseDetail

        init {
            binding.btnPhraseDetail.setOnClickListener{
                Log.e("btn", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnPhraseDetail, pos)
                }
            }
        }
        fun bind(studyResponse: StudyResponseDto) {
            phraseDetailBtn.text = studyResponse.content
        }
    }

    override fun getItemCount() = dataList.size
}