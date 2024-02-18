package com.example.pro_diction.presentation.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.databinding.SearchRecentItemBinding

class SearchAdapter(private val dataList: MutableList<StudyResponseDto>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private lateinit var binding: SearchRecentItemBinding

    interface OnItemClickListener {
        fun onItemClick(view: View, position:Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding = SearchRecentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = dataList[position]

        holder.bind(item)
    }

    inner class SearchViewHolder(val binding: SearchRecentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvSearch: TextView = binding.tvSearchRecent
        var image: ImageView = binding.ivClock
        var x : ImageView = binding.ivSearchDelete
        var tvDate: TextView = binding.tvSearchDate


        init {
            binding.tvSearchRecent.setOnClickListener{
                Log.e("btnWordRight", "true")
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(binding.tvSearchRecent, pos)
                }
            }

        }
        fun bind(search: StudyResponseDto) {
            image.visibility = View.INVISIBLE
            x.visibility = View.INVISIBLE
            tvSearch.text = search.content
            tvDate.visibility = View.INVISIBLE

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