package com.example.campuslink

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ExpandableAdapter(mutableListOf()) { node ->
            if (node.children == null) {//리프 노드 일때, 인텐트로 컬랙션 이름 Document로 넘김
                val intent = Intent(this, DocumentActivity::class.java)
                intent.putExtra("collectionName", node.collectionName)
                navigateTO(intent, "Category", node.collectionName)
            }
        }
        recyclerView.adapter = adapter

        //Document에서 버튼 클릭시 루트 설정
        val selectedCategory = intent.getStringExtra("selectedCategory")
        when (selectedCategory) {
            "Campus HUB" -> {
                val root = getCampusHubNode().children ?: emptyList()
                adapter.updateRoot(root)
            }
            "공모전/대외활동" -> {
                val root = getContestNode().children ?: emptyList()
                adapter.updateRoot(root)
            }
            "커뮤니티" -> {
                val root = getCommunityNode().children ?: emptyList()
                adapter.updateRoot(root)
            }
        }

        //버튼 클릭시 root설정
        findViewById<Button>(R.id.button11).setOnClickListener {
            val root = getCampusHubNode().children ?: emptyList()
            adapter.updateRoot(root)
        }

        findViewById<Button>(R.id.button21).setOnClickListener {
            val root = getContestNode().children ?: emptyList()
            adapter.updateRoot(root)
        }

        findViewById<Button>(R.id.button31).setOnClickListener {
            val root = getCommunityNode().children ?: emptyList()
            adapter.updateRoot(root)
        }

    }

    //컬랙션 트리
    private fun getCampusHubNode(): Node {
        return Node(
            "Campus Hub", level = 0, children = listOf(
                // 전북대 포털
                Node(
                    "| 전북대 포털", level = 1, children = listOf(
                        Node("| 교내공지", level = 2, collectionName = "전북대포털-교내공지"),
                        Node("| 교내채용", level = 2, collectionName = "전북대포털-교내채용"),
                        Node("| 학생공지", level = 2, collectionName = "전북대포털-학생공지")
                    )
                ),
                // 단과대학
                Node(
                    "| 단과대학", level = 1, children = listOf(
                        Node(
                            "| 공과대학", level = 2, children = listOf(
                                Node(
                                    "| 학부/학과", level = 3, children = listOf(
                                        Node(
                                            "| 기계설계",
                                            level = 4,
                                            collectionName = "단과대학-공과대학-학부 학과-기계설계"
                                        ),
                                        Node(
                                            "| 전자공학",
                                            level = 4,
                                            collectionName = "단과대학-공과대학-학부 학과-전자공"
                                        ),
                                        Node(
                                            "| 컴퓨터인공지능",
                                            level = 4,
                                            collectionName = "단과대학-공과대학-학부 학과-컴인지"
                                        )
                                    )
                                ), Node("| 정보광장", level = 3, collectionName = "단과대학-공과대학-정보광장-공지사항")
                            )
                        ), Node(
                            "| 자연과학대학", level = 2, children = listOf(
                                Node("| 정보광장", level = 3, collectionName = "단과대학-자연과학대학-정보광장-공지사항"),
                                Node(
                                    "| 학부/학과", level = 3, children = listOf(
                                        Node(
                                            "| 물리학과",
                                            level = 4,
                                            collectionName = "단과대학-자연과학대학-학부 학과-물리학과"
                                        ),
                                        Node(
                                            "| 수학과",
                                            level = 4,
                                            collectionName = "단과대학-자연과학대학-학부 학과-수학과"
                                        ),
                                        Node(
                                            "| 통계학과",
                                            level = 4,
                                            collectionName = "단과대학-자연과학대학-학부 학과-통계학과"
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                // 사업단
                Node(
                    "| 사업단", level = 1, children = listOf(
                        Node(
                            "| SW중심대학 사업단", level = 2, children = listOf(
                                Node(
                                    "| 온라인 신청", level = 3, children = listOf(
                                        Node(
                                            "| 프로그램 신청",
                                            level = 4,
                                            collectionName = "SW중심대학-온라인 신청-프로그램 신청"
                                        ),
                                        Node(
                                            "| J-POINT 신청 →",
                                            level = 4,
                                            url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=Point"
                                        ),
                                        Node(
                                            "| 예약", level = 4, children = listOf(
                                                Node(
                                                    "| 대관예약 →",
                                                    level = 5,
                                                    url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=923PLXM"
                                                ),
                                                Node(
                                                    "| 상담예약 →",
                                                    level = 5,
                                                    url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=928ZLHT"
                                                ),
                                                Node(
                                                    "| 장비예약 →",
                                                    level = 5,
                                                    url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=929UCHJ"
                                                ),
                                                Node(
                                                    "| 예약현황 →",
                                                    level = 5,
                                                    url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=926DSJF"
                                                )
                                            )
                                        )
                                    )
                                ), Node(
                                    "| 알림마당", level = 3, children = listOf(
                                        Node(
                                            "| 공지사항",
                                            level = 4,
                                            collectionName = "SW중심대학-알림마당-공지사항"
                                        ),
                                        Node(
                                            "| 사업단소식",
                                            level = 4,
                                            collectionName = "SW중심대학-알림마당-사업단소식"
                                        ),
                                        Node(
                                            "| 자료실",
                                            level = 4,
                                            collectionName = "SW중심대학-알림마당-자료실"
                                        )
                                    )
                                )
                            )
                        ), Node(
                            "| 빅데이터 혁신융합대학", level = 2, children = listOf(
                                Node(
                                    "| 커뮤니티", level = 3, children = listOf(
                                        Node(
                                            "| 공지사항",
                                            level = 4,
                                            collectionName = "빅데이터-커뮤니티-공지사항"
                                        ),
                                        Node(
                                            "| 뉴스",
                                            level = 4,
                                            collectionName = "빅데이터-커뮤니티-뉴스"
                                        ),
                                        Node(
                                            "| 자료실",
                                            level = 4,
                                            collectionName = "bigdata_innovation_resources"
                                        )
                                    )
                                ), Node(
                                    "| 학생지원", level = 3, children = listOf(
                                        Node(
                                            "| 서포터즈",
                                            level = 4,
                                            collectionName = "bigdata_supporters"
                                        ),
                                        Node(
                                            "| 학생인턴",
                                            level = 4,
                                            collectionName = "bigdata_intern"
                                        ),
                                        Node(
                                            "| 취창업지원",
                                            level = 4,
                                            collectionName = "bigdata_employment_support"
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                // 취업진로처
                Node(
                    "| 취업진로처", level = 1, children = listOf(
                        Node(
                            "| 취업정보", level = 2, children = listOf(
                                Node("| 채용정보", level = 3, collectionName = "취업진로처-취업정보-채용정보"),
                                Node(
                                    "| 해외채용정보",
                                    level = 3,
                                    collectionName = "취업진로처-취업정보-해외채용정보"
                                ),
                                Node("| 인턴정보", level = 3, collectionName = "취업진로처-취업정보-인턴정보"),
                                Node("| 공모전정보", level = 3, collectionName = "취업진로처-취업정보-공모전정보")
                            )
                        ), Node(
                            "| 커뮤니티", level = 2, children = listOf(
                                Node(
                                    "| 교내취업프로그램",
                                    level = 3,
                                    collectionName = "취업진로처-커뮤니티-교내취업프로그램"
                                ),
                                Node(
                                    "| 교외취업프로그램",
                                    level = 3,
                                    collectionName = "취업진로처-커뮤니티-교외취업프로그램"
                                ),
                                Node("| 취업동아리", level = 3, collectionName = "취업진로처-커뮤니티-취업동아리")
                            )
                        )
                    )
                )
            )
        )
    }

    private fun getContestNode(): Node {
        return Node(
            "공모전/대외활동", level = 0, children = listOf(
                Node(
                    "| 대외활동", level = 1, children = listOf(
                        Node(
                            "| 활동분야", level = 2, children = listOf(
                                Node("| 서포터즈", level = 3, collectionName = "대외활동-활동분야-서포터즈"),
                                Node("| 해외탐방-무료", level = 3, collectionName = "대외활동-활동분야-해외탐방(무료)"),
                                Node("| 해외탐방-유료", level = 3, collectionName = "대외활동-활동분야-해외탐방(유료)"),
                                Node("| 봉사단-해외", level = 3, collectionName = "대외활동-활동분야-봉사단(해외)"),
                                Node("| 봉사단-국내", level = 3, collectionName = "대외활동-활동분야-봉사단(국내)"),
                                Node("| 마케터", level = 3, collectionName = "대외활동-활동분야-마케터"),
                                Node("| 기자단", level = 3, collectionName = "대외활동-활동분야-기자단"),
                                Node("| 강연", level = 3, collectionName = "대외활동-활동분야-강연"),
                                Node("| 멘토링", level = 3, collectionName = "대외활동-활동분야-멘토링"),
                                Node("| 기타", level = 3, collectionName = "대외활동-활동분야-기타")
                            )
                        ), Node(
                            "| 관심분야", level = 2, children = listOf(
                                Node("| 언론/미디어", level = 3, collectionName = "대외활동-관심분야-언론 미디어"),
                                Node("| 문화/역사", level = 3, collectionName = "대외활동-관심분야-문화 역사"),
                                Node("| 경제/금융", level = 3, collectionName = "대외활동-관심분야-경제 금융"),
                                Node("| 과학/공학/기술/IT", level = 3, collectionName = "대외활동-관심분야-과학 공학 기술 IT"),
                                Node("| 요리/식품", level = 3, collectionName = "대외활동-관심분야-요리 식품"),
                                Node("| 창업/자기계발", level = 3, collectionName = "대외활동-관심분야-창업 자기계발")
                            )
                        ), Node(
                            "| 활동기간", level = 2, children = listOf(
                                Node("| 3개월 이하", level = 3, collectionName = "대외활동-활동기간-3개월이하"),
                                Node("| 3개월~6개월", level = 3, collectionName = "대외활동-활동기간-3개월~6개월"),
                                Node("| 6개월~1년", level = 3, collectionName = "대외활동-활동기간-6개월~1년"),
                                Node("| 1년 이상", level = 3, collectionName = "대외활동-활동기간-1년이상")
                            )
                        )
                    )
                ), Node(
                    "| 공모전", level = 1, children = listOf(
                        Node(
                            "| 공모분야", level = 2, children = listOf(
                                Node("| 기획/아이디어", level = 3, collectionName = "공모전-공모분야-기획 아이디어"),
                                Node("| 광고/마케팅", level = 3, collectionName = "공모전-공모분야-광고 마케팅"),
                                Node("| 사진/영상/UCC", level = 3, collectionName = "공모전-공모분야-사진 영상 UCC"),
                                Node("| 과학/공학", level = 3, collectionName = "공모전-공모분야-과학 공학")
                            )
                        ), Node(
                            "| 수상혜택", level = 2, children = listOf(
                                Node("| 입사 시 가산점", level = 3, collectionName = "공모전-수상혜택-입사가산점"),
                                Node("| 인턴/정규직 채용", level = 3, collectionName = "공모전-수상혜택-인턴 정규직채용"),
                                Node("| 해외연수/전시 기회", level = 3, collectionName = "공모전-수상혜택-해외연수 전시기회")
                            )
                        )
                    )
                )
            )
        )
    }


    private fun getCommunityNode(): Node {
        return Node(
            "커뮤니티", level = 0, children = listOf(
                Node(
                    "| 재학생 커뮤니티", level = 1, children = listOf(
                        Node("| 자유 게시판", level = 2, collectionName = "커뮤니티-재학생-자유게시판"),
                        Node("| 신입생 게시판", level = 2, collectionName = "커뮤니티-재학생-신입생게시판"),
                        Node("| 동아리 게시판", level = 2, collectionName = "커뮤니티-재학생-동아리게시판"),
                        Node("| 홍보/정보 게시판", level = 2, collectionName = "커뮤니티-재학생-홍보 정보게시판")
                    )
                ), Node(
                    "| 졸업생 커뮤니티", level = 1, children = listOf(
                        Node("| 인턴정보 게시판", level = 2, collectionName = "커뮤니티-졸업생-인턴정보게시판"),
                        Node("| 취업정보 게시판", level = 2, collectionName = "커뮤니티-졸업생-취업정보게시판"),
                        Node("| 홍보/정보 게시판", level = 2, collectionName = "커뮤니티-졸업생-홍보 정보게시판")
                    )
                ), Node(
                    "| 재학생-졸업생 커뮤니티", level = 1, children = listOf(
                        Node("| 자유 게시판", level = 2, collectionName = "커뮤니티-재학생 졸업생-자유게시판"),
                        Node("| 멘토-멘티 게시판", level = 2, collectionName = "커뮤니티-재학생 졸업생-멘토 멘티게시판")
                    )
                )
            )
        )
    }
}
