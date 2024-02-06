package com.example.pro_diction.presentation.learn

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
 * Use the [LearnPhonemeVowelsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnPhonemeVowelsFragment : Fragment() {
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
        val vowelsInflater = inflater.inflate(R.layout.fragment_learn_phoneme_vowels, container, false)
        val recyclerview = vowelsInflater.findViewById<RecyclerView>(R.id.rv_vowels)
        val consonantList: MutableList<ConsonantDto> = mutableListOf()
        consonantList.add(ConsonantDto("ㅏ"))
        consonantList.add(ConsonantDto("ㅐ"))
        consonantList.add(ConsonantDto("ㅑ"))
        consonantList.add(ConsonantDto("ㅒ"))
        consonantList.add(ConsonantDto("ㅓ"))
        consonantList.add(ConsonantDto("ㅔ"))
        consonantList.add(ConsonantDto("ㅕ"))
        consonantList.add(ConsonantDto("ㅖ"))
        consonantList.add(ConsonantDto("ㅗ"))
        consonantList.add(ConsonantDto("ㅘ"))
        consonantList.add(ConsonantDto("ㅙ"))
        consonantList.add(ConsonantDto("ㅚ"))
        consonantList.add(ConsonantDto("ㅛ"))
        consonantList.add(ConsonantDto("ㅜ"))
        consonantList.add(ConsonantDto("ㅝ"))
        consonantList.add(ConsonantDto("ㅞ"))
        consonantList.add(ConsonantDto("ㅠ"))
        consonantList.add(ConsonantDto("ㅡ"))
        consonantList.add(ConsonantDto("ㅢ"))
        consonantList.add(ConsonantDto("ㅣ"))

        val adapter = ConsonantAdapter(consonantList)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this.activity, 3)

        val intent = Intent(this.context, LearnPhonemeDetailActivity::class.java)
        adapter.setOnItemClickListener(object: ConsonantAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", consonantList[position].item)
                startActivity(intent)
            }
        })

        // floating action button
        val fab: FloatingActionButton = vowelsInflater.findViewById(R.id.fab_phoneme)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
        }

        return vowelsInflater
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LearnPhonemeVowelsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LearnPhonemeVowelsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}