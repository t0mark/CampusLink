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

class DocumentActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DocumentAdapter
    var collectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        fun goToCategory(category: String) {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("selectedCategory", category)
            navigateTO(intent, "Category", null)
        }

        // 버튼 클릭 이벤트
        findViewById<Button>(R.id.button11).setOnClickListener {
            goToCategory("Campus HUB")
        }

        findViewById<Button>(R.id.button21).setOnClickListener {
            goToCategory("공모전/대외활동")
        }

        findViewById<Button>(R.id.button31).setOnClickListener {
            goToCategory("커뮤니티")
        }


        // Intent로부터 컬렉션 이름 받기
        collectionName = intent.getStringExtra("collectionName")
        val addbtn = findViewById<FloatingActionButton>(R.id.addPostButton) //버튼 리스너(클릭 이후 양형웅)
        collectionName?.let {
            if (it.split("-")[0] == "커뮤니티" && !session.isNullOrEmpty()) {
                addbtn.visibility = View.VISIBLE//로그인상태로 커뮤니키쪽이면 보임
            } else {
                addbtn.visibility = View.GONE
            }
        } ?: run {
            Toast.makeText(this, "컬렉션 이름이 없습니다.", Toast.LENGTH_SHORT).show()
            finish() // 종료
            return
        }

        recyclerView = findViewById(R.id.documentRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 구분선 추가
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter = DocumentAdapter(mutableListOf(), collectionName!!)
        recyclerView.adapter = adapter
    }

    //문서ID 가져오기
    override fun onResume() {
        super.onResume()
        FirebaseFirestore.getInstance()
            .collection(collectionName!!)
            .get()
            .addOnSuccessListener { documents ->
                val ids = documents.map { it.id }.reversed()//역순으로 보여지게
                adapter.updateData(ids)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "데이터 로드 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // 게시물 작성 intent로 이동
    fun addPostBtnClicked(view: View) {
        BaseActivity.subPage = collectionName
        BaseActivity.isLeft = true
        val intent = Intent(this, AddCommunityActivity::class.java)
        startActivity(intent)
    }
}
