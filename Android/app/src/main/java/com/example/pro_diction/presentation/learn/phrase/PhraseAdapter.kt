package com.example.pro_diction.presentation.learn.phrase

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
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.databinding.PhraseItemBinding
import com.example.pro_diction.databinding.WordItemBinding
import com.example.pro_diction.presentation.learn.word.WordAdapter

class PhraseAdapter (private val dataList: MutableList<CategoryResponseDto>) :
    RecyclerView.Adapter<PhraseAdapter.PhraseViewHolder>() {
    private lateinit var binding: PhraseItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhraseViewHolder {
        binding = PhraseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhraseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhraseViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class PhraseViewHolder(val binding: PhraseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvPhraseMain : TextView = binding.tvPhraseMain
        var btnPhrase1: Button = binding.btnPhrase1
        var btnPhrase2: Button = binding.btnPhrase2
        var btnRight: ImageButton = binding.btnPhraseRight

        init {
            binding.icPhrase.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnPhraseRight, pos)
                }
            }
            binding.btnPhraseRight.setOnClickListener{
                Log.e("btnPhraseRight", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnPhraseRight, pos)
                }
            }
            binding.tvPhraseMain.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.tvPhraseMain, pos)
                }
            }
            binding.btnPhrase1.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnPhrase1, pos)
                }
            }
            binding.btnPhrase2.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnPhrase2, pos)
                }
            }

        }
        fun bind(categoryResponse: CategoryResponseDto) {
            tvPhraseMain.text = categoryResponse.name
            btnPhrase1.text = categoryResponse.studyResponseDtoList.get(0).content
            btnPhrase2.text = categoryResponse.studyResponseDtoList.get(1).content

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