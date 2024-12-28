package com.example.campuslink

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class DocumentMypActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentMypAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_myp)

        recyclerView = findViewById(R.id.documentMypRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // DividerItemDecoration 추가
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter = DocumentMypAdapter(mutableListOf()) // 빈 리스트로 초기화
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(session.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                // Firestore에서 데이터 가져오기
                val items = documentSnapshot.get(subPage.toString()) as? MutableList<Map<String, String>>
                // 어댑터가 이미 설정된 경우 데이터 업데이트
                adapter.updateData(items)
            }
    }
}
