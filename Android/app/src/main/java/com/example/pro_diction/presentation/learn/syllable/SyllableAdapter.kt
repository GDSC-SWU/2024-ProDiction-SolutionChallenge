package com.example.pro_diction.presentation.learn.syllable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.databinding.SyllableItemBinding

class SyllableAdapter (private val dataList: List<StudyResponseDto>) :
    RecyclerView.Adapter<SyllableAdapter.SyllableViewHolder>() {
    private lateinit var binding: SyllableItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllableViewHolder {
        binding = SyllableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyllableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyllableViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SyllableViewHolder(val binding: SyllableItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var syllableBtn : Button = binding.btnSyllable

        init {
            binding.btnSyllable.setOnClickListener{
                Log.e("btn", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnSyllable, pos)
                }
            }
        }
        fun bind(studyResponse: StudyResponseDto) {
            syllableBtn.text = studyResponse.content

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