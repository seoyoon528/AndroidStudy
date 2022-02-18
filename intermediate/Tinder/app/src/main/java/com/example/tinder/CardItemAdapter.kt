package com.example.tinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tinder.databinding.ItemCardBinding

class CardItemAdapter: androidx.recyclerview.widget.ListAdapter<CardItem, CardItemAdapter.ViewHolder> (diffUtil) {

    inner class ViewHolder(private val binding: ItemCardBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(cardItem: CardItem) {
            binding.nameTextView.text = cardItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{     // 미리 만들어진 ViewHolder가 없을 경우에 새로 생성
        return ViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {      //  ViewHolder가 View에 그려지게 되었을 때 데이터를 Bind
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {         //  RecyclerView가 View의 position이 변경되었을 때 새로운 값을 할당 할 지 말 지를 결정
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {       //  Old Item과 New Item의 Contents가 같은지 판단
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {        //  안에 있는 Contents가 같은지 다른지 판단
                return oldItem == newItem
            }

        }
    }
}