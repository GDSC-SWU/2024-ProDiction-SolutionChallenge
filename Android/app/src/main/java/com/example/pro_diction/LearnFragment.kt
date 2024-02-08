package com.example.pro_diction

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.pro_diction.databinding.FragmentLearnBinding
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeActivity
import com.example.pro_diction.presentation.learn.syllable.LearnSyllableActivity
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseActivity
import com.example.pro_diction.presentation.learn.sentense.LearnSentenseActivity
import com.example.pro_diction.presentation.learn.word.LearnWordActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LearnFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentLearnBinding

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
        binding = FragmentLearnBinding.inflate(inflater, container, false,)
        // Inflate the layout for this fragment
        val learnInflater = inflater.inflate(R.layout.fragment_learn, container, false)

        // search button 검색
        val search = learnInflater.findViewById<Button>(R.id.btn_search)
        search.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        // phoneme 음소
        learnInflater.findViewById<ImageButton>(R.id.btn_learn_1).setOnClickListener {
            val intent = Intent(requireContext(), LearnPhonemeActivity::class.java)
            startActivity(intent)
        }

        // syllable 음절
        learnInflater.findViewById<ImageButton>(R.id.btn_learn_2).setOnClickListener {
            val intent = Intent(requireContext(), LearnSyllableActivity::class.java)
            startActivity(intent)
        }

        // word 단어
        learnInflater.findViewById<ImageButton>(R.id.btn_learn_3).setOnClickListener {
            val intent = Intent(requireContext(), LearnWordActivity::class.java)
            startActivity(intent)
        }

        // phrase 구
        learnInflater.findViewById<ImageButton>(R.id.btn_learn_4).setOnClickListener {
            val intent = Intent(requireContext(), LearnPhraseActivity::class.java)
            startActivity(intent)
        }

        // sentense 문장
        learnInflater.findViewById<ImageButton>(R.id.btn_learn_5).setOnClickListener {
            val intent = Intent(requireContext(), LearnSentenseActivity::class.java)
            startActivity(intent)
        }


        return learnInflater
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LearnFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LearnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}