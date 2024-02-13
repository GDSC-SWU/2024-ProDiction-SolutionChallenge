package com.example.pro_diction.presentation.learn

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.ResponseSignInDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.presentation.learn.phoneme.ConsonantAdapter
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    var getSubCategory = ApiPool.getSubCategory
    var list: MutableList<StudyResponseDto> = mutableListOf()

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
        var adapter = ConsonantAdapter(list)

        getSubCategory.getSubCategory(2).enqueue(object:
            Callback<BaseResponse<List<StudyResponseDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<StudyResponseDto>>>,
                response: Response<BaseResponse<List<StudyResponseDto>>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        response.body()?.data?.forEach { it ->
                            list.add(it)
                        }

                        recyclerview.adapter = adapter
                        recyclerview.layoutManager = GridLayoutManager(this@LearnPhonemeVowelsFragment.activity, 3)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<StudyResponseDto>>>, t: Throwable) {
                Toast.makeText(context, "server fail", Toast.LENGTH_SHORT).show()
            }
        })

        // adapter
        adapter = ConsonantAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this.activity, 3)

        // button cick intent
        val intent = Intent(this.context, LearnPhonemeDetailActivity::class.java)
        adapter.setOnItemClickListener(object: ConsonantAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", list[position].studyId.toString())
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