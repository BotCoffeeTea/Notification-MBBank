package com.moneyreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moneyreader.R

class KeywordAdapter(
    private var keywords: MutableList<String>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword, parent, false)
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(keywords[position])
    }

    override fun getItemCount() = keywords.size

    fun updateData(newKeywords: MutableList<String>) {
        keywords = newKeywords
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        keywords.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class KeywordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keywordText: TextView = itemView.findViewById(R.id.textKeyword)

        fun bind(keyword: String) {
            keywordText.text = keyword
            itemView.setOnClickListener { onItemClicked(adapterPosition) }
        }
    }
}