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
        holder.bind(quotes[position], isNameRevealed)   // 해당 position의 quote를 전달하며 bind 호출
    }

    override fun getItemCount() = quotes.size

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote, isNameRevealed: Boolean) {
            quoteTextView.text = quote.quote

            if(isNameRevealed) {
                nameTextView.text = quote.name
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }


        }
    }

}