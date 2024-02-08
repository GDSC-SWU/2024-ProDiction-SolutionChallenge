package com.example.pro_diction.presentation.learn.word

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.databinding.SyllableItemBinding
import com.example.pro_diction.databinding.WordItemBinding
import com.example.pro_diction.presentation.learn.SyllableAdapter

class WordAdapter (private val dataList: MutableList<WordDto>) :
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
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
            binding.btnWord1.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
            binding.btnWord2.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
            binding.btnWord3.setOnClickListener{
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnWordRight, pos)
                }
            }
        }
        fun bind(word: WordDto) {
            tvWordMain.text = word.wordTitle
            btnWord1.text = word.wordList[0]
            btnWord2.text = word.wordList[1]
            btnWord3.text = word.wordList[2]

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