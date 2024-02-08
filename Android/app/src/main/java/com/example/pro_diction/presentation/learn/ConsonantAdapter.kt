package com.example.pro_diction.presentation.learn

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.databinding.ConsonantItemBinding

class ConsonantAdapter (private val dataList: MutableList<ConsonantDto>) :
    RecyclerView.Adapter<ConsonantAdapter.ConsonantViewHolder>() {
    private lateinit var binding: ConsonantItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsonantViewHolder {
        binding = ConsonantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConsonantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConsonantViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)

        /*
        holder.itemView.setOnClickListener { v ->
            // itemClickListener.onItemClick(it, position)
            Log.e("click", "click")
            val intent = Intent(v.context, LearnPhonemeDetailActivity::class.java)
            intent.putExtra("item", item.item)
            ContextCompat.startActivity(v.context, intent, null)
        }*/
    }

    inner class ConsonantViewHolder(val binding: ConsonantItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var consonantBtn : Button = binding.btnConsonant

        init {
            binding.btnConsonant.setOnClickListener{
                Log.e("btn", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.btnConsonant, pos)
                }
            }
        }
        fun bind(consonant: ConsonantDto) {
            consonantBtn.text = consonant.item

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



class ConsonantDiffCallback : DiffUtil.ItemCallback<ConsonantDto>() {
    override fun areItemsTheSame(oldItem: ConsonantDto, newItem: ConsonantDto): Boolean {
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(oldItem: ConsonantDto, newItem: ConsonantDto): Boolean {
        return oldItem == newItem
    }
}
