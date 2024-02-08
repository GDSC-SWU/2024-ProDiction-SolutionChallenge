package com.example.pro_diction.presentation.learn.phoneme

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.ConsonantDto
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LearnPhonemeConsonantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnPhonemeConsonantFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // recycler view
        val consonantInflater = inflater.inflate(R.layout.fragment_learn_phoneme_consonant, container, false)
        val recyclerview = consonantInflater.findViewById<RecyclerView>(R.id.rv_consonant)
        val consonantList: MutableList<ConsonantDto> = mutableListOf()
        consonantList.add(ConsonantDto("ㄱ"))
        consonantList.add(ConsonantDto("ㄴ"))
        consonantList.add(ConsonantDto("ㄷ"))
        consonantList.add(ConsonantDto("ㄹ"))
        consonantList.add(ConsonantDto("ㅁ"))
        consonantList.add(ConsonantDto("ㅂ"))
        consonantList.add(ConsonantDto("ㅅ"))
        consonantList.add(ConsonantDto("ㅇ"))
        consonantList.add(ConsonantDto("ㅈ"))
        consonantList.add(ConsonantDto("ㅊ"))
        consonantList.add(ConsonantDto("ㅋ"))
        consonantList.add(ConsonantDto("ㅌ"))
        consonantList.add(ConsonantDto("ㅍ"))
        consonantList.add(ConsonantDto("ㅎ"))
        consonantList.add(ConsonantDto("ㄲ"))
        consonantList.add(ConsonantDto("ㄸ"))
        consonantList.add(ConsonantDto("ㅃ"))
        consonantList.add(ConsonantDto("ㅆ"))
        consonantList.add(ConsonantDto("ㅉ"))
        // 어댑터에 리스트 연결
        val adapter = ConsonantAdapter(consonantList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this.activity, 3)
        
        val intent = Intent(this.context, LearnPhonemeDetailActivity::class.java)
        // item 클릭 시 해당 음소 페이지로 연결
        adapter.setOnItemClickListener(object: ConsonantAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", consonantList[position].item)
                startActivity(intent)
            }
        })
     

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = consonantInflater.findViewById(R.id.fab_phoneme)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
        }

        return consonantInflater    
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LearnPhonemeConsonantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LearnPhonemeConsonantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}