package com.example.campuslink

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    // View 선언
    private lateinit var db: FirebaseFirestore
    private lateinit var nameEt: EditText
    private lateinit var idEt: EditText
    private lateinit var pwEt: EditText
    private lateinit var classSpin: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // View 초기화
        db = FirebaseFirestore.getInstance()

        nameEt = findViewById<EditText>(R.id.et_register_name)
        idEt = findViewById<EditText>(R.id.et_register_id)
        pwEt = findViewById<EditText>(R.id.et_register_pw)
        classSpin = findViewById<Spinner>(R.id.sp_register_class)
        classSpin.setSelection(0, false)

        // 학과 선택
        val classOptions = listOf("선택해주세요", "컴인지", "전자공", "기계설계")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        classSpin.adapter = adapter

        val registerBtn: Button = findViewById(R.id.btn_make_user)
        registerBtn.setOnClickListener {
            val name = nameEt.text.toString()
            val id = idEt.text.toString()
            val pw = pwEt.text.toString()
            val selectedClass = classSpin.selectedItem.toString()

            if (selectedClass == "선택해주세요") {
                Toast.makeText(this, "학과를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    // ID가 이미 존재하는 경우
                    if (documentSnapshot.exists()) {
                        Toast.makeText(this, "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show()
                    // 중복된 ID가 없는 경우 -> 데이터 저장
                    } else {
                        val emptyMapList: List<Map<String, String>> = emptyList()
                        // User 정보 정규화
                        db.collection("users")
                            .document(id)
                            .set(
                                mapOf(
                                    "이름" to name,
                                    "pw" to pw,
                                    "학과" to selectedClass,
                                    "스크랩" to emptyMapList,
                                    "작성" to emptyMapList,
                                    "신청" to emptyMapList
                                )
                            )
                            .addOnSuccessListener {
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "회원가입 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                // DB 연결 실패
                .addOnFailureListener { exception ->
                    // 문서 조회 실패 처리
                    Toast.makeText(this, "회원가입 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}