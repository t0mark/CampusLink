package com.example.campuslink

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink.R

data class DocumentItem(
    val collectionName: String,
    val documentId: String
)

class ResultsAdapter(
    private val items: List<DocumentItem>
) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val documentIdTextView: TextView = view.findViewById(R.id.documentTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.documentIdTextView.text = item.documentId
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("collectionName", item.collectionName)
            intent.putExtra("documentId", item.documentId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
