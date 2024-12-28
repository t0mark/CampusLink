package com.example.campuslink

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.WindowManager
import android.widget.*
import com.example.campuslink.BaseActivity.Companion.currentPage
import com.example.campuslink.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginPopupHandler(
    private val context: Context,
    private val db: FirebaseFirestore,
    private val onLoginSuccess: (String) -> Unit
) {
    private lateinit var popupWindow: PopupWindow
    private lateinit var binding: ActivityLoginBinding

    // 로그인은 intent x -> popup window 사용
    fun showPopup(anchorView: View) {
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(context))
        val popupView = binding.root

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val popupWidth = (screenWidth * 0.8).toInt()
        val popupHeight = (screenHeight * 0.33).toInt()

        popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, -200)
        popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE

        setupPopupActions()
    }

    // View 할당 및 리스너 설정
    private fun setupPopupActions() {
        binding.register.setOnClickListener {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
            popupWindow.dismiss()
        }

        binding.loginButton.setOnClickListener {
            hideKeyboard(binding.root)
            handleLogin()
        }

        binding.closeButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    // 로그인 처리 함수
    private fun handleLogin() {
        val id = binding.id.text.toString().trim()
        val pw = binding.pw.text.toString().trim()

        if (id.isEmpty() || pw.isEmpty()) {
            Toast.makeText(context, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    if (documents != null) {
                        val matchingDocument = documents.find { it.id == id }
                        if (matchingDocument != null) {
                            val storedPw = matchingDocument.getString("pw")
                            // 로그인 성공
                            if (storedPw == pw) {
                                Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show()

                                BaseActivity.session = id
                                BaseActivity.userInfo = mapOf(
                                    "학번" to id,
                                    "이름" to (matchingDocument.getString("이름") ?: "정보 없음"),
                                    "학과" to (matchingDocument.getString("학과") ?: "정보 없음"),
                                    "스크랩" to (matchingDocument.get("스크랩") as? List<*> ?: emptyList<Any>()),
                                    "신청" to (matchingDocument.get("신청") as? List<*> ?: emptyList<Any>()),
                                    "작성" to (matchingDocument.get("작성") as? List<*> ?: emptyList<Any>())
                                )

                                popupWindow.dismiss()

                                if (currentPage == "Home") {
                                    (context as BaseActivity).navigateTo(
                                        MypageActivity::class.java,
                                        "Mypage"
                                    )
                                }
                            // ID만 일치, PW 불일치
                            } else {
                                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                            }
                        // ID 없음
                        } else {
                            Toast.makeText(context, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
                    // DB 데이터 Import 실패
                    } else {
                        Toast.makeText(context, "데이터를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    // DB 연결 실패
                } else {
                    Toast.makeText(
                        context,
                        "오류 발생: ${task.exception?.message ?: "알 수 없는 오류"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
