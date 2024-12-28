package com.example.campuslink

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 목록 그릇 디자인
class DocumentAdapter(
    private val data: MutableList<String>, // 문서 ID 리스트
    private val collectionName: String
) : RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.documentTitle) // ID 출력
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentId = data[position]

        if (documentId != "temp") {
            holder.idTextView.text = documentId.substring(11) // ID에서 앞 11자리 제거

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("collectionName", collectionName)//컬랙션 이름이랑
                intent.putExtra("documentId", documentId)//문서 ID보냄
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<String>) {
        data.clear()
        data.addAll(newData.filter { it != "temp" })
        notifyDataSetChanged()
    }
}
