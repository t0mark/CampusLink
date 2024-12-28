package com.example.campuslink

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 댓글 정보의 데이터 클래스
data class Comment(val username: String, val content: String, val timestamp: String)

// 댓글의 RecyclerView를 위한 어댑터
class CommentAdapter(private val commentList: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.text_username)
        val contentTextView: TextView = itemView.findViewById(R.id.text_content)
        val timestampTextView: TextView = itemView.findViewById(R.id.text_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentList[position]
        holder.usernameTextView.text = comment.username
        holder.contentTextView.text = comment.content
        holder.timestampTextView.text = comment.timestamp
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}
