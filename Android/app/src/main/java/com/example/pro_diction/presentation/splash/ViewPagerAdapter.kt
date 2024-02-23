package com.example.pro_diction.presentation.splash

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.pro_diction.R

class ViewPagerAdapter (private val titleList: ArrayList<String>,
    private val list: ArrayList<Int>,
    private val subList: ArrayList<String>

    ) : RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.splash_item, parent, false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_splash1)
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_splash_title)
        private val subTextView: TextView = itemView.findViewById(R.id.tv_splash_sub)

        fun bind(position: Int) {
            imageView.setImageResource(list[position])
            titleTextView.text = titleList[position]
            subTextView.text = subList[position]


        }
    }
}