package com.example.campuslink

import java.io.Serializable

data class Node(
    val name: String,                   // 항목 이름(화면에 표시됨)
    val collectionName: String? = null, // Firestore 컬렉션 이름
    val children: List<Node>? = null,   // 하위 항목 리스트
    var isExpanded: Boolean = false,    // 열려 있는지 여부
    var level: Int = 0,        // 계층 레벨
    val url: String? = null // 추가: URL 속성
) : Serializable
