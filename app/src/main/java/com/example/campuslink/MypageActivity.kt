package com.example.campuslink

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MypageActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        // User 정보 출력
        val user = userInfo
        if (user != null) {
            val name = user["이름"] as? String
            val schoolclass = user["학과"] as? String
            val idnumber = user["학번"] as? String
            findViewById<TextView>(R.id.name_db).text = name ?: "이름 없음"
            findViewById<TextView>(R.id.schoolclass_db).text = schoolclass ?: "학과 없음"
            findViewById<TextView>(R.id.idnumber_db).text =idnumber ?: "학번 없음"
        } else {
            Toast.makeText(this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        // 게시물 통합 관리 (스크랩, 신청, 작성)
        findViewById<Button>(R.id.BtnSupply).setOnClickListener {
            navigateTo(DocumentMypActivity::class.java, "Mypage", "신청")
        }

        findViewById<Button>(R.id.BtnScrap).setOnClickListener {
            navigateTo(DocumentMypActivity::class.java, "Mypage", "스크랩")
        }

        findViewById<Button>(R.id.BtnWrite).setOnClickListener {
            navigateTo(DocumentMypActivity::class.java, "Mypage", "작성")
        }
    }

    // 로그아웃
    fun logout() {
        BaseActivity.session = ""
        finish()
        navigateTo(MainActivity::class.java, "Home", null)
    }
}
