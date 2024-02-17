package com.example.pro_diction.presentation.my

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.MyWordDto
import com.example.pro_diction.data.dto.SentenseDetailDto
import com.example.pro_diction.data.dto.WordApiDto
import com.example.pro_diction.data.dto.WordListDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.sentense.SentenseDetailAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWordActivity : AppCompatActivity() {
    var getMyWord = ApiPool.getMyWord
    var deleteMyWord = ApiPool.deleteMyWord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_word)

        val type = intent.getStringExtra("type")
        // toolbar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvType: TextView = findViewById(R.id.tv_myword_type)
        when(type) {
            "1" -> {
                val stringValue = getString(R.string.learn_1)
                tvType.text = stringValue
            }
            "2" -> {
                val stringValue = getString(R.string.learn_2)
                tvType.text = stringValue
            }
            "3" -> {
                val stringValue = getString(R.string.learn_3)
                tvType.text = stringValue
            }
            "4" -> {
                val stringValue = getString(R.string.learn_4)
                tvType.text = stringValue
            }
            "5" -> {
                val stringValue = getString(R.string.learn_5)
                tvType.text = stringValue
            }
        }
        // recycler view
        val mywordList: MutableList<WordListDto> = mutableListOf()
        // 어댑터에 리스트 연결
        val adapter = MyWordAdapter(mywordList)
        // main 이 뭔지에 따라서 리스트에 넣는 값이 달라짐
        val recyclerview = findViewById<RecyclerView>(R.id.rv_myword)
        if (type != null) {
            getMyWord.getWord(type.toInt()).enqueue(object: Callback<BaseResponse<List<WordListDto>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<WordListDto>>>,
                    response: Response<BaseResponse<List<WordListDto>>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            response.body()!!.data?.forEach { it ->
                                mywordList.add(it)
                            }
                            recyclerview.adapter = adapter
                            recyclerview.layoutManager = LinearLayoutManager(this@MyWordActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<List<WordListDto>>>, t: Throwable) {
                    Log.e("error", t.toString())
                }
            })
        }


        // 어댑터에 리스트 연결
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this@MyWordActivity)

        val intent = Intent(this@MyWordActivity, LearnPhonemeDetailActivity::class.java)
        // item 클릭 시 해당 음소 페이지로 연결
        adapter.setOnItemClickListener(object: MyWordAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", mywordList[position].studyId.toString())
                startActivity(intent)
            }
        })

        adapter.setOnItemLongClickListener(object: MyWordAdapter.OnItemLongClickListener {
            override fun onItemLongClick(view: View, position: Int) {
                val builder = AlertDialog.Builder(this@MyWordActivity)
                builder.setTitle("")
                    .setMessage(getString(R.string.delete_text))
                    .setNegativeButton(getString(R.string.cancel),
                        DialogInterface.OnClickListener { dialog, id ->


                        })

                    .setPositiveButton(getString(R.string.delete),
                        DialogInterface.OnClickListener() { dialog, id ->
                            deleteMyWord.deleteWord(mywordList[position].vocabularyId).enqueue(object: Callback<BaseResponse<WordApiDto>> {
                                override fun onResponse(
                                    call: Call<BaseResponse<WordApiDto>>,
                                    response: Response<BaseResponse<WordApiDto>>
                                ) {
                                    if (response.isSuccessful) {
                                        if (response.body() != null) {
                                            Log.e("voca id", response.body().toString())
                                            Log.e("mywordList[position].vocabularyId", mywordList[position].vocabularyId.toString())
                                            mywordList.removeAt(position)
                                            adapter.notifyDataSetChanged() // 어댑터에게 데이터 변경을 알림
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<BaseResponse<WordApiDto>>,
                                    t: Throwable
                                ) {
                                    Log.e("error", t.toString())
                                }
                            })

                        })
                builder.show()
            }
        })

        // floating action button 플로팅 버튼 연결
        val fab: FloatingActionButton = findViewById(R.id.fab_myword)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
        }

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
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
