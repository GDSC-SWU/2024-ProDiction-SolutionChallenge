package com.example.pro_diction.presentation.learn.sentense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.PhraseDto
import com.example.pro_diction.data.dto.SentenseDto
import com.example.pro_diction.presentation.learn.SearchActivity
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.phrase.LearnPhraseDetailActivity
import com.example.pro_diction.presentation.learn.phrase.PhraseAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LearnSentenseActivity : AppCompatActivity() {
    var getCategory = ApiPool.getCategory
    var list: MutableList<CategoryResponseDto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_sentense)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_sentense)
        var adapter = SentenseAdapter(list)

        // api
        getCategory.getCategory(5, false, 2).enqueue(object:
            Callback<BaseResponse<List<CategoryResponseDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                response: Response<BaseResponse<List<CategoryResponseDto>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.forEach { it ->
                        list.add(it)
                    }

                    recyclerview.adapter = adapter
                    recyclerview.layoutManager = LinearLayoutManager(this@LearnSentenseActivity)
                }
            }

            override fun onFailure(
                call: Call<BaseResponse<List<CategoryResponseDto>>>,
                t: Throwable
            ) {
                Toast.makeText(this@LearnSentenseActivity, "server fail", Toast.LENGTH_SHORT).show()
            }
        })

        // adapter
        adapter = SentenseAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        // button click
        adapter.setOnItemClickListener(object: SentenseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                when (view.id) {
                    R.id.btn_sentense_right -> {
                        val intent = Intent(this@LearnSentenseActivity, LearnSentenseDetailActivity::class.java)
                        intent.putExtra("id", list[position].id.toString())
                        intent.putExtra("name", list[position].name.toString())
                        startActivity(intent)
                    }
                    R.id.tv_sentense_main -> {
                        val intent = Intent(this@LearnSentenseActivity, LearnSentenseDetailActivity::class.java)
                        intent.putExtra("id", list[position].id.toString())
                        intent.putExtra("name", list[position].name.toString())
                        startActivity(intent)
                    }
                    R.id.btn_sentense1 -> {
                        val intent = Intent(this@LearnSentenseActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", list[position].studyResponseDtoList.get(0).studyId.toString())
                        startActivity(intent)
                    }
                    R.id.btn_sentense2 -> {
                        val intent = Intent(this@LearnSentenseActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", list[position].studyResponseDtoList.get(1).studyId.toString())
                        startActivity(intent)
                    }
                }

            }
        })

        // floating action button
        val fab: FloatingActionButton = findViewById(R.id.fab_sentense)
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
                // activity
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
