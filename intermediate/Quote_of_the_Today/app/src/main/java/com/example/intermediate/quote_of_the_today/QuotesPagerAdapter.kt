package com.example.intermediate.quote_of_the_today

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
): RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder (
                LayoutInflater.from(parent.context)     // parent의 context 가져옴
                    .inflate(R.layout.item_quote, parent, false)
                )

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size     // 마지막 quote의 다음을 첫번째 quote로 설정하여 무한 Swipe

        holder.bind(quotes[actualPosition], isNameRevealed)   // 해당 position의 quote를 전달하며 bind 호출
    }

    override fun getItemCount() = Int.MAX_VALUE     // 페이지를 무한대로 Swipe 할 수 있게하기 위해 item 개수를 크게 설정

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote, isNameRevealed: Boolean) {
            quoteTextView.text = "\" ${quote.quote}\""          // " 명언 " Format

            if(isNameRevealed) {
                nameTextView.text  = "- ${quote.name}"
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }


        }
    }

}