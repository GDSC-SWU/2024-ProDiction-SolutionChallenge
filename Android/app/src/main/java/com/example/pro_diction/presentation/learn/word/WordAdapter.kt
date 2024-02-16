package com.example.pro_diction.presentation.learn.word

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.databinding.WordItemBinding

class WordAdapter (private val dataList: MutableList<CategoryResponseDto>) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
    private lateinit var binding: WordItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        binding = WordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class WordViewHolder(val binding: WordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvWordMain : TextView = binding.tvWordMain
        var btnWord1: Button = binding.btnWord1
        var btnWord2: Button = binding.btnWord2
        var btnWord3: Button = binding.btnWord3
        var btnRight: ImageButton = binding.btnWordRight

        init {
            binding.icWord.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
            binding.btnWordRight.setOnClickListener{
                Log.e("btnWordRight", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
            binding.tvWordMain.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.tvWordMain, pos)
                }
            }
            binding.btnWord1.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWord1, pos)
                }
            }
            binding.btnWord2.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWord2, pos)
                }
            }
            binding.btnWord3.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWord3, pos)
                }
            }
        }
        fun bind(categoryResponse: CategoryResponseDto) {
            tvWordMain.text = categoryResponse.name
            btnWord1.text = categoryResponse.studyResponseDtoList.get(0).content
            btnWord2.text = categoryResponse.studyResponseDtoList.get(1).content
            btnWord3.text = categoryResponse.studyResponseDtoList.get(2).content

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