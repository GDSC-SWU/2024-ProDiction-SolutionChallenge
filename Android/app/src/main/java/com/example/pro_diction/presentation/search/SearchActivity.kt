package com.example.pro_diction.presentation.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.R
import com.example.pro_diction.data.ApiPool
import com.example.pro_diction.data.BaseResponse
import com.example.pro_diction.data.dto.CategoryResponseDto
import com.example.pro_diction.data.dto.SearchDeleteDto
import com.example.pro_diction.data.dto.SearchRecentDto
import com.example.pro_diction.data.dto.StudyResponseDto
import com.example.pro_diction.presentation.learn.phoneme.LearnPhonemeDetailActivity
import com.example.pro_diction.presentation.learn.word.LearnWordDetailActivity
import com.example.pro_diction.presentation.learn.word.WordAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    var list: MutableList<SearchRecentDto> = mutableListOf()
    var getSearchRecent = ApiPool.getSearchRecent
    var postSearch = ApiPool.postSearch
    var deleteSearch = ApiPool.deleteSearch
    var listSearch: MutableList<StudyResponseDto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        findViewById<TextView>(R.id.tv_recent_search).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tv_del).visibility = View.VISIBLE

        // toolbar
        val toolbar: Toolbar = findViewById(R.id.tb_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // edit text
        findViewById<EditText>(R.id.edit_search).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력 전에 호출됨
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출됨
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력 후에 호출됨
                val text = s.toString()
                if (text.isEmpty()) {
                    // EditText가 비어있을 때 특정 코드를 실행
                    // 여기에 원하는 동작을 추가하세요
                    finish()
                    val intent = Intent(this@SearchActivity, SearchActivity::class.java)
                    intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    val editText = findViewById<EditText>(R.id.edit_search)
                    val inputMethodManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        })

        // recycler view
        val recyclerview = findViewById<RecyclerView>(R.id.rv_recent_search)
        var adapter = SearchRecentAdapter(list)
        var adapterSearch = SearchAdapter(listSearch, findViewById<EditText>(R.id.edit_search))

        getSearchRecent.getSearchRecent().enqueue(object: Callback<BaseResponse<List<SearchRecentDto>>> {
            override fun onResponse(
                call: Call<BaseResponse<List<SearchRecentDto>>>,
                response: Response<BaseResponse<List<SearchRecentDto>>>
            ) {
                if (response.isSuccessful) {
                    list.clear()
                    if (response.body() != null) {
                        response.body()!!.data?.forEach { it ->
                            list.add(it)
                        }

                        recyclerview.adapter = adapter
                        recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponse<List<SearchRecentDto>>>, t: Throwable) {
                Log.e("error", t.toString())
            }
        })

        // adapter
        adapter = SearchRecentAdapter(list)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object: SearchRecentAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                when (view.id) {
                    R.id.tv_search_recent -> {
                        findViewById<EditText>(R.id.edit_search).setText(list[position].searchContent.toString())
                        postSearch.postSearch(list[position].searchContent.toString()).enqueue(object: Callback<BaseResponse<List<StudyResponseDto>>> {
                            override fun onResponse(
                                call: Call<BaseResponse<List<StudyResponseDto>>>,
                                response: Response<BaseResponse<List<StudyResponseDto>>>
                            ) {
                                if (response.isSuccessful) {
                                    listSearch.clear()
                                    if (response.body() != null) {
                                        response.body()!!.data?.forEach {it ->
                                            listSearch.add(it)
                                        }
                                        findViewById<TextView>(R.id.tv_recent_search).visibility = View.INVISIBLE
                                        findViewById<TextView>(R.id.tv_del).visibility = View.INVISIBLE

                                        recyclerview.adapter = adapterSearch
                                        recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)

                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<BaseResponse<List<StudyResponseDto>>>,
                                t: Throwable
                            ) {
                                Log.e("error", t.toString())
                            }
                        })
                        // adapter
                        adapterSearch = SearchAdapter(listSearch, findViewById<EditText>(R.id.edit_search))
                        recyclerview.adapter = adapterSearch
                        recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)

                        adapterSearch.setOnItemClickListener(object: SearchAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                when (view.id) {
                                    R.id.tv_search_recent -> {
                                        val intent =
                                            Intent(this@SearchActivity, LearnPhonemeDetailActivity::class.java)
                                        intent.putExtra("item", listSearch[position].studyId.toString())
                                        startActivity(intent)
                                    }
                                }
                            }
                        })

                    }
                    R.id.iv_search_delete -> {
                        Log.e("click", "click")
                        deleteSearch.deleteSearch(list[position].searchId).enqueue(object: Callback<BaseResponse<SearchDeleteDto>> {
                            override fun onResponse(
                                call: Call<BaseResponse<SearchDeleteDto>>,
                                response: Response<BaseResponse<SearchDeleteDto>>
                            ) {
                                if (response.isSuccessful) {
                                    if (response.body() != null) {
                                        finish()
                                        val intent = Intent(this@SearchActivity, SearchActivity::class.java)
                                        intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                        startActivity(intent)
                                    }
                                }
                            }

                            override fun onFailure(
                                call: Call<BaseResponse<SearchDeleteDto>>,
                                t: Throwable
                            ) {
                                Log.e("error", t.toString())
                            }
                        })
                    }

                }

            }
        })

        var editText = findViewById<EditText>(R.id.edit_search)
        editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 엔터를 눌렀을 때 실행할 동작을 여기에 추가
                // 예를 들어, 버튼을 클릭하거나 특정 함수를 호출할 수 있습니다.
                postSearch.postSearch(editText.text.toString()).enqueue(object: Callback<BaseResponse<List<StudyResponseDto>>> {
                    override fun onResponse(
                        call: Call<BaseResponse<List<StudyResponseDto>>>,
                        response: Response<BaseResponse<List<StudyResponseDto>>>
                    ) {
                        if (response.isSuccessful) {
                            listSearch.clear()
                            if (response.body() != null) {
                                response.body()!!.data?.forEach {it ->
                                    listSearch.add(it)
                                }
                                findViewById<TextView>(R.id.tv_recent_search).visibility = View.INVISIBLE
                                findViewById<TextView>(R.id.tv_del).visibility = View.INVISIBLE

                                recyclerview.adapter = adapterSearch
                                recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)

                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseResponse<List<StudyResponseDto>>>,
                        t: Throwable
                    ) {
                        Log.e("error", t.toString())
                    }
                })

                // adapter
                adapterSearch = SearchAdapter(listSearch, findViewById<EditText>(R.id.edit_search))
                recyclerview.adapter = adapterSearch
                recyclerview.layoutManager = LinearLayoutManager(this)

                adapterSearch.setOnItemClickListener(object: SearchAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        when (view.id) {
                            R.id.tv_search_recent -> {
                                val intent =
                                    Intent(this@SearchActivity, LearnPhonemeDetailActivity::class.java)
                                intent.putExtra("item", listSearch[position].studyId.toString())
                                startActivity(intent)
                            }
                        }
                    }
                })



                true // 이벤트가 소비되었음을 반환
            } else {
                false // 이벤트를 소비하지 않음을 반환
            }
        }

        adapterSearch.setOnItemClickListener(object: SearchAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                when (view.id) {
                    R.id.tv_search_recent -> {
                        val intent =
                            Intent(this@SearchActivity, LearnPhonemeDetailActivity::class.java)
                        intent.putExtra("item", listSearch[position].studyId.toString())
                        startActivity(intent)
                    }
                }
            }
        })
    }

    fun highlightText(textView: TextView, fullText: String, targetText: String, color: Int) {
        val spannable = SpannableStringBuilder(fullText)
        val startIndex = fullText.indexOf(targetText)
        if (startIndex != -1) {
            val endIndex = startIndex + targetText.length
            spannable.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannable
        }
    }
    /*
    // 툴바 메뉴 버튼 설정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu_open, menu)
        return true
    }*/

    // 툴바 메뉴 클릭 됐을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish()
                return true
            }
            /*
            R.id.menu_search -> { // 검색 버튼
                val intent = Intent(this@SearchActivity, SearchActivity::class.java)
                startActivity(intent)
            }*/
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}