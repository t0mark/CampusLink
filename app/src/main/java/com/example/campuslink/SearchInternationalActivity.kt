package com.example.campuslink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SearchInternationalActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultTextView: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var resultsAdapter: ResultsAdapter
    private val allResults = mutableListOf<DocumentItem>()
    private val filteredResults = mutableListOf<DocumentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_international)

        searchEditText = findViewById(R.id.searchEditText)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        resultsRecyclerView.addItemDecoration(dividerItemDecoration)

        resultTextView = findViewById(R.id.resultTextView)
        db = FirebaseFirestore.getInstance()

        resultsAdapter = ResultsAdapter(filteredResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = resultsAdapter

        resultTextView.text = "대외활동 데이터를 불러오는 중입니다..."

        fetchAllData()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                filterResults(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchAllData() {
        val collectionPaths = listOf(
            "/대외활동-관심분야-경제 금융",
            "/대외활동-관심분야-과학 공학 기술 IT",
            "/대외활동-관심분야-문화 역사",
            "/대외활동-관심분야-언론 미디어",
            "/대외활동-관심분야-요리 식품",
            "/대외활동-관심분야-창업 자기계발",
            "/대외활동-활동기간-1년이상",
            "/대외활동-활동기간-3개월~6개월",
            "/대외활동-활동기간-3개월이하",
            "/대외활동-활동기간-6개월~1년",
            "/대외활동-활동분야-강연",
            "/대외활동-활동분야-기자단",
            "/대외활동-활동분야-기타",
            "/대외활동-활동분야-마케터",
            "/대외활동-활동분야-멘토링",
            "/대외활동-활동분야-봉사단(국내)",
            "/대외활동-활동분야-봉사단(해외)",
            "/대외활동-활동분야-서포터즈",
            "/대외활동-활동분야-해외탐방(무료)",
            "/대외활동-활동분야-해외탐방(유료)"
        )

        allResults.clear()
        filteredResults.clear()

        collectionPaths.forEach { collectionPath ->
            db.collection(collectionPath)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val documentId = document.id
                        val url = document.getString("URL")
                        allResults.add(DocumentItem(collectionPath, documentId))
                    }

                    filteredResults.clear()
                    filteredResults.addAll(allResults)
                    resultsAdapter.notifyDataSetChanged()

                    if (filteredResults.isEmpty()) {
                        resultTextView.visibility = View.VISIBLE
                        resultTextView.text = "데이터를 찾을 수 없습니다."
                    } else {
                        resultTextView.visibility = View.GONE
                    }
                }
                .addOnFailureListener {
                    if (filteredResults.isEmpty()) {
                        resultTextView.visibility = View.VISIBLE
                        resultTextView.text = "데이터를 불러오지 못했습니다."
                    }
                }
        }
    }

    private fun filterResults(query: String) {
        if (query.isEmpty()) {
            filteredResults.clear()
            filteredResults.addAll(allResults)
        } else {
            filteredResults.clear()
            filteredResults.addAll(allResults.filter {
                it.documentId.contains(query, ignoreCase = true)
            })
        }

        resultsAdapter.notifyDataSetChanged()

        if (filteredResults.isEmpty()) {
            resultTextView.visibility = View.VISIBLE
            resultTextView.text = "검색 결과가 없습니다."
        } else {
            resultTextView.visibility = View.GONE
        }
    }
}