package com.example.campuslink

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

abstract class BaseActivity : AppCompatActivity() {
    // 전역적으로 공유되는 변수
    companion object {
        // 로그인 정보
        var session: String? = null
        var userInfo: Map<String, Any>? = null

        // 페이지 정보
        var currentPage: String? = null
        var subPage: String? = null
        // 애니메이션: true -> Left, false -> Right
        var isLeft = true
    }

    private lateinit var loginPopupHandler: LoginPopupHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 로그인
        loginPopupHandler = LoginPopupHandler(this, FirebaseFirestore.getInstance()) { userId ->
            session = userId
        }

        // 애니메이션 설정
        if (isLeft) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } else {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    // 애니메이션 설정
    override fun finish() {
        super.finish()
        if (isLeft) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    // 뒤로가기 버튼 처리
    override fun onBackPressed() {
        // 기본 페이지에서 뒤로가기 -> App 종료
        if (BaseActivity.subPage.isNullOrEmpty()) {
            super.onBackPressed()
        // 서브 페이지에서 뒤로가기 -> 기본 페이지로 이동
        } else {
            if (BaseActivity.currentPage == "Category") {
                navigateTo(CategoryActivity::class.java, "Category", null)
            } else if (BaseActivity.currentPage == "Home") {
                navigateTo(MainActivity::class.java, "Home", null)
            } else {
                navigateTo(MypageActivity::class.java, "Mypage", null)
            }
        }
    }

    // 하단 내비게이션바 추가
    override fun setContentView(layoutResID: Int) {
        val baseLayout = layoutInflater.inflate(R.layout.activity_base, null)
        val container = baseLayout.findViewById<FrameLayout>(R.id.container)

        layoutInflater.inflate(layoutResID, container, true)
        super.setContentView(baseLayout)

        setupNavigationBar()
    }

    private fun setupNavigationBar() {
        val categoryButton = findViewById<ImageView>(R.id.Category)
        val homeButton = findViewById<ImageView>(R.id.Home)
        val myPageButton = findViewById<ImageView>(R.id.Mypage)

        categoryButton.setOnClickListener {
            navigateTo(CategoryActivity::class.java, "Category", null)
        }

        homeButton.setOnClickListener {
            navigateTo(MainActivity::class.java, "Home", null)
        }

        myPageButton.setOnClickListener {
            if (session.isNullOrEmpty()) {
                loginPopupHandler.showPopup(it)
            } else {
                navigateTo(MypageActivity::class.java, "Mypage", null)
            }
        }
    }

    // 페이지 이동 함수
    public fun navigateTo(activityClass: Class<out AppCompatActivity>?, nextPage: String, nextSubPage: String?=null) {
        if (this::class.java != activityClass) {
            setAnime(nextPage, nextSubPage)
            val intent = Intent(this, activityClass)
            startActivity(intent)
            finish()

            BaseActivity.currentPage = nextPage
            BaseActivity.subPage = nextSubPage
        }
    }

    public fun navigateTO(intent: Intent?, nextPage: String, nextSubPage: String?=null){
        if (intent != null) {
            setAnime(nextPage, nextSubPage)
            startActivity(intent)
            finish()


            BaseActivity.currentPage = nextPage
            BaseActivity.subPage = nextSubPage
        }
    }

    public fun setAnime(nextPage: String, nextSubPage: String?=null) {
        if (nextPage == "Category") {
            if (BaseActivity.currentPage == "Category") {
                // 카테고리 -> document
                if (BaseActivity.subPage.isNullOrEmpty()) {
                    BaseActivity.isLeft = true
                // document -> 카테고리
                } else {
                    BaseActivity.isLeft = false
                }
            // 다른 페이지 -> 카테고리
            } else {
                BaseActivity.isLeft = true
            }
        } else if (nextPage == "Mypage") {
            if (BaseActivity.currentPage == "Mypage") {
                // 마이페이지 -> 서브 페이지
                if (BaseActivity.subPage.isNullOrEmpty()) {
                    BaseActivity.isLeft = false
                // 서브 페이지 -> 마이페이지
                } else {
                    BaseActivity.isLeft = true
                }
            // 다른 페이지 -> 마이페이지
            } else {
                BaseActivity.isLeft = false
            }
        } else { // nextPage == "Home"
            // 다른 페이지 -> 홈
            if (BaseActivity.currentPage == "Category") {
                BaseActivity.isLeft = false
            } else if (BaseActivity.currentPage == "Mypage"){
                BaseActivity.isLeft = true
            // 홈 -> 홈
            } else {
                // 홈 -> 검색
                if (BaseActivity.subPage.isNullOrEmpty()) {
                    BaseActivity.isLeft = true
                } else {
                    BaseActivity.isLeft = false
                }
            }
        }
    }
}
