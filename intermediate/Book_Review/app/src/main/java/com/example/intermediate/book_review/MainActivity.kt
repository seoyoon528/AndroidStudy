package com.example.intermediate.book_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intermediate.book_review.adapter.BookAdapter
import com.example.intermediate.book_review.api.BookService
import com.example.intermediate.book_review.databinding.ActivityMainBinding
import com.example.intermediate.book_review.model.BestSellerDto
import com.example.intermediate.book_review.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var bookService: BookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)       // binding 가져옴
        setContentView(binding.root)

        initBookRecyclerView()

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
                        Log.d("MainActivity", it.toString())
                        it.books.forEach {book ->
                            Log.d("MainActivity", book.toString())
                        }

                        adapter.submitList(response.body()?.books.orEmpty())        // 리사이클러뷰 갱신
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e("MainActivity", t.toString())
                }
            })

        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {        // 검색어 입력 후 엔터 누르면
                search(binding.searchEditText.text.toString())      //  새로운 리스트를 불러와 RecyclerView에 띄워줌
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String) {
        bookService.getBookByName(getString(R.string.interparkAPIKey), keyword)
            .enqueue(object: Callback<SearchBookDto> {
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (response.isSuccessful.not()) {      // 성공처리
                        return
                    }

                    response.body()?.let {
                        Log.d("MainActivity", it.toString())
                        it.books.forEach {book ->
                            Log.d("MainActivity", book.toString())
                        }

                        adapter.submitList(response.body()?.books.orEmpty())        // 리사이클러뷰 갱신
                    }
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.e("MainActivity", t.toString())
                }
            })
    }

    fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }
}