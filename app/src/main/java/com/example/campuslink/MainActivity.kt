package com.example.campuslink

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 현재 페이지를 Home으로 설정
        BaseActivity.currentPage = "Home"

        val noticeButton: TextView = findViewById(R.id.noticeButton)
        val contestButton : TextView = findViewById(R.id.contestButton)
        val internationalButton : TextView = findViewById(R.id.internationalButton)

        noticeButton.setOnClickListener {
            navigateTo(SearchNoticeActivity::class.java, "Home", "공지사항")
        }
        contestButton.setOnClickListener {
            navigateTo(SearchContestActivity::class.java, "Home", "공모전")
        }
        internationalButton.setOnClickListener {
            navigateTo(SearchInternationalActivity::class.java, "Home", "대외활동")
        }


    }
    fun openSearchActivity(view: android.view.View) {
        navigateTo(SearchActivity::class.java, "Home", "종합")
    }


    // 서브 기능 -> 인터넷 App -> URL 연결
    fun onButton1Clicked(view: View) {
        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ieilms.jbnu.ac.kr/"))
        startActivity(myIntent)
    }

    fun onButton2Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://dl.jbnu.ac.kr/"))
        startActivity(myIntent)
    }

    fun onButton3Clicked(view: View) {
        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://career.jbnu.ac.kr/sites/career/index.do"))
        startActivity(myIntent)
    }

    fun onButton4Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://oasis.jbnu.ac.kr/com/login.do"))
        startActivity(myIntent)
    }

    fun onButton5Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://chatgpt.com/"))
        startActivity(myIntent)
    }

    fun onButton6Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://papago.naver.com/"))
        startActivity(myIntent)
    }

    fun onButton7Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://portal.jbnu.ac.kr/web/index.do"))
        startActivity(myIntent)
    }

    fun onButton8Clicked(view: View) {
        val myIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://oasis.jbnu.ac.kr/jbnu/sugang/index.html"))
        startActivity(myIntent)
    }
}

