package com.moneyreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moneyreader.R

class RegexAdapter(
    private var regexPatterns: MutableList<String>,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<RegexAdapter.RegexViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegexViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_regex, parent, false)
        return RegexViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegexViewHolder, position: Int) {
        holder.bind(regexPatterns[position])
    }

    override fun getItemCount() = regexPatterns.size

    fun updateData(newPatterns: MutableList<String>) {
        regexPatterns = newPatterns
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        regexPatterns.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class RegexViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val regexText: TextView = itemView.findViewById(R.id.textRegex)

        fun bind(pattern: String) {
            regexText.text = pattern
            itemView.setOnClickListener { onItemClicked(adapterPosition) }
        }
    }
}