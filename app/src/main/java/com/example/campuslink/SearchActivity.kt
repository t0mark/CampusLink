package com.example.campuslink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink.R
import com.example.campuslink.ResultsAdapter
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration

class SearchActivity : BaseActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var resultsAdapter: ResultsAdapter
    private val searchResults = mutableListOf<DocumentItem>()
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.searchEditText)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)

        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        resultsRecyclerView.addItemDecoration(dividerItemDecoration)

        resultTextView = findViewById(R.id.resultTextView)
        db = FirebaseFirestore.getInstance()

        resultsAdapter = ResultsAdapter(searchResults)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = resultsAdapter

        resultTextView.text = "검색어를 입력해주세요."

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                if (searchText.isNotEmpty()) {
                    resultTextView.visibility = View.GONE
                    searchInAllCollections(searchText)
                } else {
                    resultTextView.visibility = View.VISIBLE
                    resultTextView.text = "검색어를 입력해주세요."
                    searchResults.clear()
                    resultsAdapter.notifyDataSetChanged()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun searchInAllCollections(query: String) {
        val collections = listOf(
            "/공모전-공모분야-과학 공학",
            "/공모전-공모분야-광고 마케팅",
            "/공모전-공모분야-기획 아이디어",
            "/공모전-공모분야-사진 영상 UCC",
            "/공모전-수상혜택-인턴 정규직채용",
            "/공모전-수상혜택-입사가산점",
            "/공모전-수상혜택-해외연수 전시기회",
            "/SW중심대학-알림마당-공지사항",
            "/SW중심대학-알림마당-사업단소식",
            "/SW중심대학-알림마당-자료실",
            "/SW중심대학-온라인 신청-프로그램 신청",
            "/단과대학-공과대학-정보광장-공지사항",
            "/단과대학-공과대학-학부 학과-기계설계",
            "/단과대학-공과대학-학부 학과-전자공",
            "/단과대학-공과대학-학부 학과-컴인지",
            "/단과대학-자연과학대학-정보광장-공지사항",
            "/단과대학-자연과학대학-학부 학과-물리학과",
            "/단과대학-자연과학대학-학부 학과-수학과",
            "/단과대학-자연과학대학-학부 학과-통계학과",
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
            "/대외활동-활동분야-해외탐방(유료)",
            "/빅데이터-커뮤니티-공지사항",
            "/빅데이터-커뮤니티-뉴스",
            "/전북대포털-교내공지",
            "/전북대포털-교내채용",
            "/전북대포털-학생공지",
            "/취업진로처-취업정보-공모전정보",
            "/취업진로처-취업정보-인턴정보",
            "/취업진로처-취업정보-채용정보",
            "/취업진로처-취업정보-해외채용정보",
            "/취업진로처-커뮤니티-교내취업프로그램",
            "/취업진로처-커뮤니티-교외취업프로그램",
            "/커뮤니티-재학생-자유게시판"
        )

        searchResults.clear()
        var completedRequests = 0

        for (collection in collections) {
            db.collection(collection)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val documentId = document.id
                        if (documentId.contains(query, ignoreCase = true)) {
                            searchResults.add(DocumentItem(collection, documentId))
                        }
                    }

                    completedRequests++
                    if (completedRequests == collections.size) {
                        resultsAdapter.notifyDataSetChanged()

                        if (searchResults.isEmpty()) {
                            resultTextView.visibility = View.VISIBLE
                            resultTextView.text = "검색 결과가 없습니다."
                        } else {
                            resultTextView.visibility = View.GONE
                        }
                    }
                }
                .addOnFailureListener {
                    completedRequests++
                    if (completedRequests == collections.size) {
                        resultsAdapter.notifyDataSetChanged()

                        if (searchResults.isEmpty()) {
                            resultTextView.visibility = View.VISIBLE
                            resultTextView.text = "검색 결과가 없습니다."
                        } else {
                            resultTextView.visibility = View.GONE
                        }
                    }
                }
        }
    }
}
