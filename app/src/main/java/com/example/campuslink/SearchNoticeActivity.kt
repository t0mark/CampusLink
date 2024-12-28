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
import com.example.campuslink.R
import com.google.firebase.firestore.FirebaseFirestore

class SearchNoticeActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var resultTextView: TextView
    private lateinit var db: FirebaseFirestore
    private lateinit var resultsAdapter: ResultsAdapter
    private val allResults = mutableListOf<DocumentItem>()
    private val filteredResults = mutableListOf<DocumentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_notice)

        searchEditText = findViewById(R.id.searchEditText)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        resultsRecyclerView.addItemDecoration(dividerItemDecoration)

        resultTextView = findViewById(R.id.resultTextView)
        db = FirebaseFirestore.getInstance()

        resultsAdapter = ResultsAdapter(filteredResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = resultsAdapter

        resultTextView.text = "공지사항을 불러오는 중입니다..."

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
            "/SW중심대학-알림마당-공지사항",
            "/단과대학-공과대학-정보광장-공지사항",
            "/단과대학-공과대학-학부 학과-기계설계",
            "/단과대학-공과대학-학부 학과-전자공",
            "/단과대학-공과대학-학부 학과-컴인지",
            "/단과대학-자연과학대학-정보광장-공지사항",
            "/단과대학-자연과학대학-학부 학과-물리학과",
            "/단과대학-자연과학대학-학부 학과-수학과",
            "/단과대학-자연과학대학-학부 학과-통계학과",
            "/빅데이터-커뮤니티-공지사항",
            "/빅데이터-커뮤니티-뉴스",
            "/전북대포털-교내공지",
            "/전북대포털-교내채용",
            "/전북대포털-학생공지",
            "/취업진로처-커뮤니티-교내취업프로그램",
            "/취업진로처-커뮤니티-교외취업프로그램",
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
