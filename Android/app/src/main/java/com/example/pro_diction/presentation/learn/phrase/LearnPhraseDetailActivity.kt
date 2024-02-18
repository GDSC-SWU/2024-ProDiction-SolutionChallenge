package com.example.pro_diction.presentation.learn.phrase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.PhraseDetailDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.data.dto.WordDetailDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.word.WordDetailAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LearnPhraseDetailActivity : AppCompatActivity() {
    var getSubCategory = ApiPool.getSubCategory
    var list: MutableList<StudyResponseDto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_phrase_detail)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // consonant setting
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        var tvMain = findViewById<TextView>(R.id.tv_phrase_detail_main)
        tvMain.text = name

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_phrase_detail)
        var adapter = PhraseDetailAdapter(list)

        // api
        if (id != null) {
            getSubCategory.getSubCategory(id.toInt()).enqueue(object :
                Callback<BaseResponse<List<StudyResponseDto>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<StudyResponseDto>>>,
                    response: Response<BaseResponse<List<StudyResponseDto>>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.forEach { it ->
                            list.add(it)
                        }

                        recyclerview.adapter = adapter
                        recyclerview.layoutManager = LinearLayoutManager(this@LearnPhraseDetailActivity)
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<StudyResponseDto>>>,
                    t: Throwable
                ) {
                    Toast.makeText(this@LearnPhraseDetailActivity, "server fail", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // adpater
        adapter = PhraseDetailAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this@LearnPhraseDetailActivity)

        val intent = Intent(this@LearnPhraseDetailActivity, LearnPhonemeDetailActivity::class.java)
        // button click intent
        adapter.setOnItemClickListener(object: PhraseDetailAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                intent.putExtra("item", list[position].studyId.toString())
                startActivity(intent)
            }
        })

        // floating action button
        val fab: FloatingActionButton = findViewById(R.id.fab_phrase_detail)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
        }


    }
    // toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // toolbar menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar back click
                // activity move
                finish()
                return true
            }
            R.id.menu_search -> { // search button
                val intent = Intent(this, SearchActivity::class.java)
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
