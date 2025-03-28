package com.abc.uchat.homepage.onechats

import android.os.Message
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

class onechatviewmodel @Inject constructor() : ViewModel(){
    var _messages =  MutableStateFlow<List<Messages>>(emptyList())
    val messages = _messages.asStateFlow()
    private val db = Firebase.database

    fun listenForMessages(channelID: String) {
        db.getReference("messages").child(channelID).orderByChild("createdAt")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list1 = mutableListOf<Messages>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Messages::class.java)
                        message?.let {
                            list1.add(it)
                        }
                    }
                    _messages.value = list1
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

    }


    fun sendmesseage(channelID: String , message: String){
        val message = Messages(
            db.reference.push().key ?: UUID.randomUUID().toString() ,
            Firebase.auth.currentUser?.uid ?:"" ,
            messages = message ,
            createAt = System.currentTimeMillis() ,
            Firebase.auth.currentUser?.displayName ?:"" ,
            null , null
        )

        db.getReference("messages").child(channelID).push().setValue(message)
    }



}