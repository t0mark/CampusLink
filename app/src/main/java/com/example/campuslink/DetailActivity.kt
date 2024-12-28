package com.example.campuslink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration

class DetailActivity : BaseActivity() {
    private lateinit var db: FirebaseFirestore

    private lateinit var titleView: TextView
    private lateinit var authorView: TextView
    private lateinit var dateView: TextView
    private lateinit var recruitmentView: TextView
    private lateinit var locationView: TextView
    private lateinit var applicationPeriodView: TextView
    private lateinit var activityPeriodView: TextView
    private lateinit var imageViews: List<ImageView>
    private lateinit var contentView: TextView
    private lateinit var supportLinkView: TextView
    private lateinit var attachmentsContainer: LinearLayout
    private lateinit var siteButton: Button
    private lateinit var scrapButton: CheckBox
    private lateinit var doneButton: CheckBox

    private lateinit var commentInputField: EditText
    private lateinit var commentSubmitButton: Button
    private lateinit var commentRecyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 뷰 참조
        titleView = findViewById(R.id.detailTitle)
        authorView = findViewById(R.id.detailAuthor)
        dateView = findViewById(R.id.detailDate)
        recruitmentView = findViewById(R.id.detailRecruitment)
        locationView = findViewById(R.id.detailLocation)
        applicationPeriodView = findViewById(R.id.detailApplicationPeriod)
        activityPeriodView = findViewById(R.id.detailActivityPeriod)
        imageViews = listOf(
            findViewById<ImageView>(R.id.imageView1),
            findViewById<ImageView>(R.id.imageView2),
            findViewById<ImageView>(R.id.imageView3),
            findViewById<ImageView>(R.id.imageView4),
            findViewById<ImageView>(R.id.imageView5)
        )
        contentView = findViewById(R.id.detailContent)
        supportLinkView = findViewById(R.id.detailSupportLink)
        attachmentsContainer = findViewById(R.id.detailAttachmentsContainer)
        siteButton = findViewById(R.id.siteButton)
        scrapButton = findViewById(R.id.scrapBox)
        doneButton = findViewById(R.id.doneBox)

        commentInputField = findViewById(R.id.commentInputField)
        commentSubmitButton = findViewById(R.id.commentSubmitButton)

        commentRecyclerView = findViewById(R.id.commentsRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(this)

        //구분선
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        commentRecyclerView.addItemDecoration(dividerItemDecoration)

        commentAdapter = CommentAdapter(commentList)
        commentRecyclerView.adapter = commentAdapter

        // Intent 컬랙션이랑 문서ID 받음
        val collectionName = intent.getStringExtra("collectionName") ?: return
        val documentId = intent.getStringExtra("documentId") ?: return

        db = FirebaseFirestore.getInstance()

        // 게시물 필드값들 받기
        db.collection(collectionName)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val title = document.getString("제목")
                    if (!title.isNullOrEmpty()) {
                        titleView.text = title
                    } else {
                        titleView.visibility = TextView.GONE
                    }

                    val author = document.getString("작성자")
                    if (!author.isNullOrEmpty()) {
                        authorView.text = "작성자: $author"
                    } else {
                        authorView.visibility = TextView.GONE
                    }

                    val date = document.getString("작성일")
                    if (!date.isNullOrEmpty()) {
                        dateView.text = "작성일: $date"
                    } else {
                        dateView.visibility = TextView.GONE
                    }

                    val recruitment = document.getString("모집인원")
                    if (!recruitment.isNullOrEmpty()) {
                        recruitmentView.text = "모집인원: $recruitment"
                    } else {
                        recruitmentView.visibility = TextView.GONE
                    }

                    val location = document.getString("활동지역")
                    if (!location.isNullOrEmpty()) {
                        locationView.text = "활동지역: $location"
                    } else {
                        locationView.visibility = TextView.GONE
                    }

                    val applicationPeriod = document.getString("접수기간")
                    if (!applicationPeriod.isNullOrEmpty()) {
                        applicationPeriodView.text = "접수기간: $applicationPeriod"
                    } else {
                        applicationPeriodView.visibility = TextView.GONE
                    }

                    val activityPeriod = document.getString("활동기간")
                    if (!activityPeriod.isNullOrEmpty()) {
                        activityPeriodView.text = "활동기간: $activityPeriod"
                    } else {
                        activityPeriodView.visibility = TextView.GONE
                    }

                    val content = document.getString("내용")
                    if (!content.isNullOrEmpty()) {
                        contentView.text = content
                    } else {
                        contentView.visibility = TextView.GONE
                    }

                    // 이미지 링크를
                    val imageLinks = document.get("이미지 링크") as? List<String>
                    if (imageLinks != null && imageLinks.isNotEmpty()) {
                        // 각 ImageView에 이미지 로드
                        imageLinks.forEachIndexed { index, imageUrl ->
                            //일단 최대 5개로 설정함.
                            if (index < imageViews.size) {
                                imageViews[index].visibility = ImageView.VISIBLE
                                Glide.with(this)//Glide라이브러리 사용
                                    .load(imageUrl)
                                    .override(
                                        Resources.getSystem().displayMetrics.widthPixels,
                                        Target.SIZE_ORIGINAL
                                    )
                                    .fitCenter()
                                    .into(imageViews[index])
                            }
                        }
                    }

                    val supportLink = document.getString("지원 링크")
                    if (collectionName.split('-')[0] == "공모전" || collectionName.split('-')[0] == "대외활동") {
                        supportLinkView.visibility = View.VISIBLE//링커리어만 있게 함
                    } else {
                        supportLinkView.visibility = View.GONE
                    }
                    if (!supportLink.isNullOrEmpty()) {//url이동
                        supportLinkView.text = supportLink
                        supportLinkView.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(supportLink))
                            startActivity(intent)
                        }
                    } else {
                        supportLinkView.visibility = TextView.GONE
                    }

                    val attachments = document.get("첨부파일") as? List<Map<String, String>>
                    attachments?.forEach { attachment ->
                        val fileName = attachment["첨부파일이름"]
                        val fileLink = attachment["첨부파일링크"]

                        if (!fileName.isNullOrEmpty() && !fileLink.isNullOrEmpty()) {
                            val fileTextView = TextView(this).apply {
                                text = fileName//이름 띄우고
                                setPadding(0, 8, 0, 8)
                                setTextColor(resources.getColor(android.R.color.holo_blue_dark))
                                setOnClickListener {//링크연결
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fileLink))
                                    startActivity(intent)
                                }
                            }
                            attachmentsContainer.addView(fileTextView)
                        }
                    }

                    val url = document.getString("URL")
                    if ("커뮤니티" == collectionName.split('-')[0]) {
                        siteButton.visibility = View.INVISIBLE//커뮤니티는 안보이게-마지막 바뀐 부분.
                    } else {
                        siteButton.visibility = View.VISIBLE
                    }
                    siteButton.setOnClickListener {
                        if (!url.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                    }

                    if (session.isNullOrEmpty()) {//로그인 정보
                        scrapButton.visibility = View.INVISIBLE
                        doneButton.visibility = View.INVISIBLE
                    } else {
                        // 접속 시 스크랩 버튼 표시
                        scrapButton.visibility = View.VISIBLE
                        db.collection("users")
                            .document(session.toString())
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                val items =
                                    documentSnapshot.get("스크랩") as? List<Map<String, String>>
                                if (items != null) {
                                    for (item in items) {
                                        if (item["컬렉션 이름"] == collectionName && item["문서 이름"] == documentId) {
                                            scrapButton.isChecked = true//필드에 있는 값과 같으면 참
                                            break
                                        } else {
                                            scrapButton.isChecked = false
                                        }
                                    }
                                }
                            }
                        // 스크랩 체크시 처리
                        scrapButton.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {//체크되면 필드값 배열에 추가
                                db.collection("users")
                                    .document(session.toString())
                                    .update(
                                        "스크랩", FieldValue.arrayUnion(
                                            mapOf(
                                                "컬렉션 이름" to collectionName,
                                                "문서 이름" to documentId
                                            )
                                        )
                                    )
                            } else {
                                db.collection("users")
                                    .document(session.toString())
                                    .update(
                                        "스크랩", FieldValue.arrayRemove(//else 제거
                                            mapOf(
                                                "컬렉션 이름" to collectionName,
                                                "문서 이름" to documentId
                                            )
                                        )
                                    )
                            }
                        }

                        //신청 버튼 스크랩과 동일
                        doneButton.visibility = View.VISIBLE
                        db.collection("users")
                            .document(session.toString())
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                val items = documentSnapshot.get("신청") as? List<Map<String, String>>
                                if (items != null) {
                                    for (item in items) {
                                        if (item["컬렉션 이름"] == collectionName && item["문서 이름"] == documentId) {
                                            doneButton.isChecked = true
                                            break
                                        } else {
                                            doneButton.isChecked = false
                                        }
                                    }
                                }
                            }
                        // 신청 체크시 처리 스크랩과 동일
                        doneButton.setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                db.collection("users")
                                    .document(session.toString())
                                    .update(
                                        "신청", FieldValue.arrayUnion(
                                            mapOf(
                                                "컬렉션 이름" to collectionName,
                                                "문서 이름" to documentId
                                            )
                                        )
                                    )

                            } else {
                                db.collection("users")
                                    .document(session.toString())
                                    .update(
                                        "신청", FieldValue.arrayRemove(
                                            mapOf(
                                                "컬렉션 이름" to collectionName,
                                                "문서 이름" to documentId
                                            )
                                        )
                                    )
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "문서를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "데이터 로드 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        fetchCommentsFromFirestore(collectionName, documentId)
        setupCommentSubmission(collectionName, documentId)
    }

    /////////////////////이 아래는 양현웅/////////////////////////////////
    // 댓글 출력 함수
    private fun fetchCommentsFromFirestore(collection: String, documentId: String) {
        db.collection(collection)
            .document(documentId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val comments = documentSnapshot["댓글"] as? List<Map<String, Any>>
                    if (comments != null) {
                        commentList.clear()
                        for (comment in comments) {
                            val username = comment["댓글 작성자"] as? String ?: "Unknown"
                            val content = comment["댓글"] as? String ?: "No Content"
                            val timestamp = comment["작성시간"] as? String ?: "No Timestamp"
                            commentList.add(Comment(username, content, timestamp))
                        }
                        commentAdapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "댓글 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
    }

    // 댓글 업로드 함수
    private fun setupCommentSubmission(
        collection: String,
        documentId: String
    ) {
        commentSubmitButton.setOnClickListener {
            hideKeyboard()

            val content = commentInputField.text.toString().trim()
            val timestamp = SimpleDateFormat(
                "yyyy.MM.dd HH:mm",
                Locale.getDefault()
            ).format(Date(System.currentTimeMillis()))

            if ((session ?: "").isNotEmpty()) {
                if (content.isNotEmpty()) {
                    val newComment = mapOf(
                        "댓글 작성자" to session,
                        "댓글" to content,
                        "작성시간" to timestamp
                    )
                    db.collection(collection)
                        .document(documentId)
                        .update("댓글", FieldValue.arrayUnion(newComment))
                        .addOnSuccessListener {
                            commentInputField.text.clear()
                            Toast.makeText(this, "댓글이 추가되었습니다!", Toast.LENGTH_SHORT).show()
                            fetchCommentsFromFirestore(collection, documentId)
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "댓글 추가에 실패", Toast.LENGTH_SHORT).show()
                        }
                // 입력값이 비어 있을 경우
                } else {
                    Toast.makeText(this, "댓글 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            // 로그인 x -> 로그인 Toast 메시지
            } else {
                Toast.makeText(this, "로그인을 먼저 해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 댓글 업로드 후, 터치 키보드 지우기
    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
