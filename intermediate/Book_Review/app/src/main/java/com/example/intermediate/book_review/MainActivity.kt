package com.example.intermediate.book_review

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.intermediate.book_review.adapter.BookAdapter
import com.example.intermediate.book_review.adapter.HistoryAdapter
import com.example.intermediate.book_review.api.BookService
import com.example.intermediate.book_review.databinding.ActivityMainBinding
import com.example.intermediate.book_review.model.BestSellerDto
import com.example.intermediate.book_review.model.History
import com.example.intermediate.book_review.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)       // binding 가져옴
        setContentView(binding.root)

        db = getAppDatabase(this)

        initBookRecyclerView()      // 도서 목록 리사이클러뷰 초기화
        initHistoryRecyclerView()       //  검색 기록 리사이클러뷰 초기화
        initSearchEditText()        // 검색어 입력 EditText 초기화

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks(getString(R.string.interparkAPIKey))
            .enqueue(object: Callback<BestSellerDto> {
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) {      // 성공처리
                        return
                    }

                    response.body()?.let {
//                        Log.d("MainActivity", it.toString())
                        it.books.forEach {book ->
//                            Log.d("MainActivity", book.toString())
                        }
                        adapter.submitList(response.body()?.books.orEmpty())        // 리사이클러뷰 갱신
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e("MainActivity", t.toString())
                }
            })
    }

    private fun search(keyword: String) {
        bookService.getBookByName(getString(R.string.interparkAPIKey), keyword)
            .enqueue(object: Callback<SearchBookDto> {
                override fun onResponse(call: Call<SearchBookDto>, response: Response<SearchBookDto>) {     // 서치 성공

                    hideHistoryRecyclerView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {      // 성공처리
                        return
                    }

                    response.body()?.let {
//                        Log.d("MainActivity", it.toString())
                        it.books.forEach {book ->
//                            Log.d("MainActivity", book.toString())
                        }

                        adapter.submitList(response.body()?.books.orEmpty())        // 리사이클러뷰 갱신
                    }
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {       //  서치 실패
                    hideHistoryRecyclerView()
                    Log.e("MainActivity", t.toString())
                }
            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)       // 'Book' class 자체를 넘김
            startActivity(intent)
        })
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter (historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        }, clickItemSearchListener = {search(it)})
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {        // 검색어 입력 후 엔터 누르면
                search(binding.searchEditText.text.toString())      //  새로운 리스트를 불러와 RecyclerView에 띄워줌
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        
        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryRecyclerView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryRecyclerView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()      //  최신 순서대로 키워드 기록을 보여줌

            runOnUiThread{
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()

        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryRecyclerView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))       // HistoryDB에 keyword 저장
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryRecyclerView()       // 뷰 갱신
        }.start()
    }

}