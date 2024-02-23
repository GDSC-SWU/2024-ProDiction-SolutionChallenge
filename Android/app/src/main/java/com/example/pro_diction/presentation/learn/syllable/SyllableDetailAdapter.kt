package com.example.pro_diction.presentation.learn.syllable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.databinding.SyllableDetailItemBinding

class SyllableDetailAdapter (private val dataList: MutableList<StudyResponseDto>) :
    RecyclerView.Adapter<SyllableDetailAdapter.SyllableDetailViewHolder>() {
    private lateinit var binding: SyllableDetailItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyllableDetailViewHolder {
        binding = SyllableDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SyllableDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyllableDetailViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SyllableDetailViewHolder(val binding: SyllableDetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var syllableBtn : Button = binding.btnSyllable

        init {
            binding.btnSyllable.setOnClickListener{
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