package com.example.pro_diction.presentation.learn.syllable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pro_diction.HangulConverter
import com.example.pro_diction.R
import com.example.pro_diction.data.dto.ConsonantDto
import com.example.pro_diction.presentation.learn.SearchActivity

class LearnSyllableActivity : AppCompatActivity() {
    var ja: MutableList<ConsonantDto> = mutableListOf()
    var resultList: MutableList<ConsonantDto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_syllable)

        // tool bar
        val toolbar: Toolbar = findViewById(R.id.tb_phoneme)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // recycler view
        //val consonantInflater = inflater.inflate(R.layout.fragment_learn_phoneme_consonant, container, false)
        val recyclerview = findViewById<RecyclerView>(R.id.rv_syllable)
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
        val adapter = SyllableAdapter(consonantList)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = GridLayoutManager(this, 3)

        // parser
        ja = consonantList
        var result = ArrayList<String>()



        // spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        val items = resources.getStringArray(R.array.spinner_array)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> {
                        setList('ㅏ')
                    }
                    1 -> {
                        setList('ㅑ')
                    }
                    2 -> {
                        setList('ㅓ')
                    }
                    3 -> {
                        setList('ㅕ')
                    }
                    4 -> {
                        setList('ㅗ')
                    }
                    5 -> {
                        setList('ㅛ')
                    }
                    6 -> {
                        setList('ㅜ')
                    }
                    7 -> {
                        setList('ㅠ')
                    }
                    8 -> {
                        setList('ㅡ')
                    }
                    9 -> {
                        setList('ㅣ')
                    }
                    10 -> {
                        setList('ㅐ')
                    }
                    11 -> {
                        setList('ㅒ')
                    }
                    12 -> {
                        setList('ㅔ')
                    }
                    13 -> {
                        setList('ㅖ')
                    }
                    14 -> {
                        setList('ㅚ')
                    }
                    15 -> {
                        setList('ㅟ')
                    }
                    16 -> {
                        setList('ㅢ')
                    }
                    17 -> {
                        setList('ㅘ')
                    }
                    18 -> {
                        setList('ㅝ')
                    }
                    19 -> {
                        setList('ㅙ')
                    }
                    20 -> {
                        setList('ㅞ')
                    }

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
        }
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

    fun setList(jungsung: Char) {
        resultList.clear()
        for (i in ja) {
            val hangulChar = HangulConverter.joinHangul(i.item[0], jungsung)
            resultList.add(ConsonantDto(hangulChar.toString()))
        }
    }


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
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
