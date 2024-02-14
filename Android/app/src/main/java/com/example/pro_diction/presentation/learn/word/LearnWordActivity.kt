package com.example.pro_diction.presentation.learn.word

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.WordDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.ConsonantAdapter
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LearnWordActivity : AppCompatActivity() {
    var getCategory = ApiPool.getCategory
    var list: MutableList<CategoryResponseDto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_word)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_word)
        var adapter = WordAdapter(list)

        getCategory.getCategory(3, false, 3).enqueue(object: Callback<BaseResponse<List<CategoryResponseDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                response: Response<BaseResponse<List<CategoryResponseDto>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.forEach { it ->
                        list.add(it)
                    }

                    recyclerview.adapter = adapter
                    recyclerview.layoutManager = LinearLayoutManager(this@LearnWordActivity)
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                t: Throwable
            ) {
                Toast.makeText(this@LearnWordActivity, "server fail", Toast.LENGTH_SHORT).show()
            }
        })

        // adapter
        adapter = WordAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: WordAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                when (view.id) {
                    R.id.btn_word_right -> {
                        val intent = Intent(this@LearnWordActivity, LearnWordDetailActivity::class.java)
                        intent.putExtra("id", list[position].id.toString())
                        intent.putExtra("name", list[position].name.toString())
                        startActivity(intent)
                    }
                    R.id.tv_word_main -> {
                        val intent = Intent(this@LearnWordActivity, LearnWordDetailActivity::class.java)
                        intent.putExtra("id", list[position].id.toString())
                        intent.putExtra("name", list[position].name.toString())
                        startActivity(intent)
                    }
                    R.id.btn_word1 -> {
                        val intent = Intent(this@LearnWordActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", list[position].studyResponseDtoList.get(0).studyId.toString())
                        startActivity(intent)
                    }
                    R.id.btn_word2 -> {
                        val intent = Intent(this@LearnWordActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", list[position].studyResponseDtoList.get(1).studyId.toString())
                        startActivity(intent)
                    }
                    R.id.btn_word3 -> {
                        val intent = Intent(this@LearnWordActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", list[position].studyResponseDtoList.get(2).studyId.toString())
                        startActivity(intent)
                    }

                }

            }
        })

        // floating action button
        val fab: FloatingActionButton = findViewById(R.id.fab_word)
        fab.setOnClickListener {
            recyclerview.smoothScrollToPosition(0)
        }
    }
    // toolbar menu button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // toolbar menu click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar back button
                // activity move
                finish()
                return true
            }
            R.id.menu_search -> { // search button
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}