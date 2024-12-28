package com.example.campuslink

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class DocumentMypAdapter (
    private val items: MutableList<Map<String, String>>?
) : RecyclerView.Adapter<DocumentMypAdapter.ViewHolder>() {

    private val data: MutableList<String> = mutableListOf()

    // ViewHolder 정의
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.documentTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // items에서 position에 해당하는 Map을 가져옴
        val item = items?.get(position)
        val documentId = item?.get("문서 이름") ?: "Unknown Document"
        val collectionName = item?.get("컬렉션 이름") ?: "Unknown Collection"

        // "문서 이름" 처리
        holder.idTextView.text = if (documentId.length > 11) {
            documentId.substring(11)
        } else {
            documentId // 길이가 짧을 경우 그대로 표시
        }

        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("collectionName", collectionName)
            intent.putExtra("documentId", documentId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items!!.size

    // 데이터 업데이트 함수 추가
    fun updateData(newItems: List<Map<String, String>>?) {
        items?.clear() // 기존 데이터 초기화
        newItems?.let { items?.addAll(it) } // 새로운 데이터 추가
        notifyDataSetChanged() // RecyclerView 갱신
    }
}