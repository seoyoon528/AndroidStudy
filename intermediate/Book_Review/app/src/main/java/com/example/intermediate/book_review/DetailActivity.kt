package com.example.intermediate.book_review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.intermediate.book_review.databinding.ActivityDetailBinding
import com.example.intermediate.book_review.model.Book
import com.example.intermediate.book_review.model.Review

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")        // intent 가져옴

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverLargeUrl.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)       // db에서 가져올 땐 null처리 주의
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {     // 리뷰 저장
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,     //  id == null인 경우 0
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }
}