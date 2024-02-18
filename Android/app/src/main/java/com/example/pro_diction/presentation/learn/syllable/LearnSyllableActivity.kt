package com.example.pro_diction.presentation.learn.syllable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.presentation.search.SearchActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LearnSyllableActivity : AppCompatActivity() {
    var ja: MutableList<ConsonantDto> = mutableListOf()
    var resultList: MutableList<ConsonantDto> = mutableListOf()
    val getCategory = ApiPool.getCategory // api
    val list: MutableList<CategoryResponseDto> = mutableListOf()
    var itemList: MutableList<StudyResponseDto> = mutableListOf()
    var studtList: MutableList<StudyResponseDto> = mutableListOf()
    val getSubCategory = ApiPool.getSubCategory // api
    var studyList: MutableList<StudyResponseDto> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_syllable)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinnerList = mutableListOf<String>()

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_syllable)

        // api
        getCategory.getCategory(2, false, 0).enqueue(object: Callback<BaseResponse<List<CategoryResponseDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                response: Response<BaseResponse<List<CategoryResponseDto>>>
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        Log.e("isSuccessful response", response.body().toString())
                        Log.e("isSuccessful   response.body()", response.body()?.data?.get(0)?.id.toString())

                        response.body()?.data?.forEach { it ->
                            list.add(it)
                            spinnerList.add(it.name)
                        }

                        // spinner vowels
                        val spinnerAdapter = ArrayAdapter(this@LearnSyllableActivity, android.R.layout.simple_spinner_dropdown_item, spinnerList)
                        spinner.adapter = spinnerAdapter
                    } else {
                        Log.e("NotSuccessful", response.toString())
                    }
                } else {
                    response.body()?.let { Log.e("Response", it.toString()) }
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                t: Throwable
            ) {
                Toast.makeText(this@LearnSyllableActivity , "서버 통신을 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        // recycler view adpater
        val adapter = SyllableAdapter(studyList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this, 3)

        // parser
        // ja = consonantList
        var result = ArrayList<String>()


        Log.e("spinnerList", spinnerList.toString())


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                // set items
                // setList(p2)
                val categoryId = list.get(p2).id
                studyList.clear()


                getSubCategory.getSubCategory(categoryId).enqueue(object: Callback<BaseResponse<List<StudyResponseDto>>>  {
                    override fun onResponse(
                        call: Call<BaseResponse<List<StudyResponseDto>>>,
                        response: Response<BaseResponse<List<StudyResponseDto>>>
                    ) {
                        Log.e("response", response.body().toString())
                        response.body()?.data?.forEach { it ->
                            studyList.add(it)
                        }
                        recyclerview.adapter = adapter
                        recyclerview.layoutManager = GridLayoutManager(this@LearnSyllableActivity, 3)

                    }

                    override fun onFailure(
                        call: Call<BaseResponse<List<StudyResponseDto>>>,
                        t: Throwable
                    ) {
                        TODO("Not yet implemented")
                    }
                })

                // item recycler view adapter
                val adapter = SyllableAdapter(studyList)

                recyclerview.adapter = adapter
                recyclerview.layoutManager = GridLayoutManager(this@LearnSyllableActivity, 3)


            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinner.setSelection(0)
            }


        }


        val intent = Intent(this@LearnSyllableActivity, LearnSyllableDetailActivity::class.java)
        adapter.setOnItemClickListener(object: SyllableAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("studyId", studyList[position].studyId.toString())
                intent.putExtra("content", studyList[position].content.toString())
                startActivity(intent)
            }
        })
        /*
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> {
                        resultList.clear()
                        for(i in ja) {
                            result.add(i.item)
                            result.add("ㅏ")
                            var final = parser.assem(result)
                            resultList.add(ConsonantDto(final))
                            result.clear()
                        }
                    }
                    1 -> {
                        resultList.clear()
                        for(i in ja) {
                            result.add(i.item)
                            result.add("ㅑ")
                            var final = parser.assem(result)
                            resultList.add(ConsonantDto(final))
                            result.clear()
                        }


                    }
                    // 전부 추가하기
                    else -> {

                    }
                }
                val adapter = SyllableAdapter(resultList)

                recyclerview.adapter = adapter
                recyclerview.layoutManager = GridLayoutManager(this@LearnSyllableActivity, 3)

                val intent = Intent(this@LearnSyllableActivity, LearnSyllableDetailActivity::class.java)
                adapter.setOnItemClickListener(object: SyllableAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        intent.putExtra("item", resultList[position].item)
                        startActivity(intent)
                    }
                })

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinner.setSelection(0)
            }
        }*/

    }

    /*
    fun setList(jungsung: Char) {
        resultList.clear()
        for (i in ja) {
            val hangulChar = HangulConverter.joinHangul(i.item[0], jungsung)
            resultList.add(ConsonantDto(hangulChar.toString()))
        }
    }*/


    // 툴바 메뉴 버튼 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // 툴바 메뉴 클릭 됐을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish()
                return true
            }
            R.id.menu_search -> { // 검색 버튼
                val intent = Intent(this, SearchActivity::class.java)
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
