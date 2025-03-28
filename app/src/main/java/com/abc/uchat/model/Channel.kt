package com.abc.uchat.model

class Channel(
    val id:String ,
    val name:String  ,
    val createdAt:Long = System.currentTimeMillis()

)