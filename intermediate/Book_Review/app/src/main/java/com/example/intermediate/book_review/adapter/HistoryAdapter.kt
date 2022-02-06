package com.example.intermediate.book_review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.intermediate.book_review.MainActivity
import com.example.intermediate.book_review.databinding.ItemHistoryBinding
import com.example.intermediate.book_review.model.History

//  historyDeleteClickedListener: (String) -> Unit :: MainActivity에서 HistoryAdapter에 함수를 던져서 ClickListener가 일어날 때 함수를 호출하는 방식
class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit, val clickItemSearchListener: (String) -> Unit): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
                binding.historyKeywordTextView.text = historyModel.keyword
                binding.historyKeywordDeleteButton.setOnClickListener {
                    historyDeleteClickedListener(historyModel.keyword.orEmpty())
                }

                binding.root.setOnClickListener {       //  검색 기록 클릭 시 도서 재 검색
                    clickItemSearchListener(historyModel.keyword.orEmpty())
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {     // 미리 만들어진 ViewHolder가 없을 경우에 새로 생성
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {      //  ViewHolder가 View에 그려지게 되었을 때 데이터를 Bind
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<History>() {         //  RecyclerView가 View의 position이 변경되었을 때 새로운 값을 할당 할 지 말 지를 결정
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {       //  Old Item과 New Item의 Contents가 같은지 판단
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {        //  안에 있는 Contents가 같은지 다른지 판단
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}