package com.example.campuslink

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.*

// 게시물 작성 화면
class AddCommunityActivity: BaseActivity(){

    // View 선언
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var submitPostButton: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_community)

        // View 초기화
        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        submitPostButton = findViewById(R.id.submitPostButton)

        db = FirebaseFirestore.getInstance()

        // 게시물 작성 완료 버튼
        submitPostButton.setOnClickListener {
            // 저장 정보 : 제목, 내용, 작성일, 작성자
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val currentTime = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date())
            val documentId = "${currentTime}_$title"

            if (title.isBlank() || content.isBlank()) {
                Toast.makeText(this, "제목과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection(subPage.toString())
                .document(documentId)
                .set(
                    // 다른 게시물과 필드 형식은 통일 (정규화)
                    mapOf(
                        "url" to null,
                        "내용" to content,
                        "모집인원" to null,
                        "이미지 링크" to null,
                        "작성일" to currentTime,
                        "작성자" to session.toString(),
                        "접수기간" to null,
                        "제목" to title,
                        "지원 링크" to null,
                        "첨부파일" to null,
                        "활동기간" to null,
                        "활동지역" to null
                    )
                )
                .addOnSuccessListener {
                    Toast.makeText(this, "게시물 작성 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "게시물 작성 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}