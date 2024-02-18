package com.example.pro_diction.presentation.search

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.databinding.SearchRecentItemBinding

class SearchAdapter(private val dataList: MutableList<StudyResponseDto>, private val editText: EditText) :
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

            // EditText의 텍스트와 TextView의 텍스트를 비교하여 같은 글자가 있으면 색상 변경
            val editTextValue = editText.text.toString()
            if (editTextValue.isNotEmpty() && tvSearch.text.contains(editTextValue)) {
                val startIndex = tvSearch.text.indexOf(editTextValue)
                val endIndex = startIndex + editTextValue.length
                val spannable = SpannableStringBuilder(tvSearch.text)
                spannable.setSpan(
                    ForegroundColorSpan(Color.parseColor("#2F4C74")),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tvSearch.text = spannable
            } else {
                tvSearch.setTextColor(Color.parseColor("#565656")) // 같은 글자가 없으면 기본 색상으로 변경
            }

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