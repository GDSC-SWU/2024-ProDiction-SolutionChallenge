package com.example.pro_diction.presentation.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.databinding.SearchRecentItemBinding
import com.example.pro_diction.databinding.WordItemBinding
import com.example.pro_diction.presentation.learn.word.WordAdapter

class SearchRecentAdapter(private val dataList: MutableList<SearchRecentDto>) :
    RecyclerView.Adapter<SearchRecentAdapter.SearchRecentViewHolder>() {
    private lateinit var binding: SearchRecentItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecentViewHolder {
        binding = SearchRecentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchRecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchRecentViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SearchRecentViewHolder(val binding: SearchRecentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvSearchDate : TextView = binding.tvSearchDate
        var tvSearchRecent: TextView = binding.tvSearchRecent
        var x : ImageView = binding.ivSearchDelete


        init {
            binding.ivSearchDelete.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.ivSearchDelete, pos)
                }
            }
            binding.tvSearchRecent.setOnClickListener{
                Log.e("btnWordRight", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.tvSearchRecent, pos)
                }
            }

        }
        fun bind(searchRecent: SearchRecentDto) {
            tvSearchDate.text = searchRecent.searchDate
            tvSearchRecent.text = searchRecent.searchContent


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