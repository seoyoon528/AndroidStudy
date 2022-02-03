package com.example.intermediate.book_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intermediate.book_review.adapter.BookAdapter
import com.example.intermediate.book_review.api.BookService
import com.example.intermediate.book_review.databinding.ActivityMainBinding
import com.example.intermediate.book_review.model.BestSellerDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)       // binding 가져옴
        setContentView(binding.root)

        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("FE685BD543BAA8B6568A5DF5E2FD6092C751B37EF3236312FB56BB537A24B67A")
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

                        adapter.submitList(it.books)
                    }

                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e("MainActivity", t.toString())
                }

            })


//        bookService.getBookByName("FE685BD543BAA8B6568A5DF5E2FD6092C751B37EF3236312FB56BB537A24B67A")

    }

    fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }
}