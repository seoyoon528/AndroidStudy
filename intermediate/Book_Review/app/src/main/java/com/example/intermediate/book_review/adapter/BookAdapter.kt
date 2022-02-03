package com.example.intermediate.book_review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.intermediate.book_review.databinding.ItemBookBinding
import com.example.intermediate.book_review.model.Book

class BookAdapter: ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class BookItemViewHolder(private val binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel: Book) {
            binding.titleTextView.text = bookModel.title        // 뷰 바인딩
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {     // 미리 만들어진 ViewHolder가 없을 경우에 새로 생성
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {      //  ViewHolder가 View에 그려지게 되었을 때 데이터를 Bind
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {         //  RecyclerView가 View의 position이 변경되었을 때 새로운 값을 할당 할 지 말 지를 결정
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {       //  Old Item과 New Item의 Contents가 같은지 판단
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {        //  안에 있는 Contents가 같은지 다른지 판단
                return oldItem.id == newItem.id
            }

        }
    }
}