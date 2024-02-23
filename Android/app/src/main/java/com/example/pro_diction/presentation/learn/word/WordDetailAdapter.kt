package com.example.pro_diction.presentation.learn.word

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.data.dto.WordDetailDto
import com.example.pro_diction.databinding.SyllableItemBinding
import com.example.pro_diction.databinding.WordDetailItemBinding
import com.example.pro_diction.presentation.learn.syllable.SyllableAdapter

class WordDetailAdapter (private val dataList: MutableList<StudyResponseDto>) :
    RecyclerView.Adapter<WordDetailAdapter.WordDetailViewHolder>() {
    private lateinit var binding: WordDetailItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordDetailViewHolder {
        binding = WordDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordDetailViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class WordDetailViewHolder(val binding: WordDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var wordDetailBtn : Button = binding.btnWordDetail

        init {
            binding.btnWordDetail.setOnClickListener{
                Log.e("btn", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordDetail, pos)
                }
            }
        }
        fun bind(studyResponse: StudyResponseDto) {
            wordDetailBtn.text = studyResponse.content

            /*
            view.setOnClickListener {
                Log.e("onClick", "onclick")
                val intent = Intent(view.context, LearnPhonemeDetailActivity::class.java).apply {
                    putExtra("item", consonant.item)

                }
                view.context.startActivity(intent)
            }*/
        }
    }

    override fun getItemCount() = dataList.size
}