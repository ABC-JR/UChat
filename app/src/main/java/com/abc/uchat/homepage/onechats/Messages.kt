package com.abc.uchat.homepage.onechats


data class Messages(
    val id: String = "",
    val senderId: String = "",
    val messages: String = "",
    val createAt: Long = System.currentTimeMillis(),
    val senderName: String = "",
    val senderImage: String? = null,
    val imgurl: String? = null
) {
    // Обязательный пустой конструктор для Firebase
    constructor() : this("", "", "", 0L, "", null, null)
}
