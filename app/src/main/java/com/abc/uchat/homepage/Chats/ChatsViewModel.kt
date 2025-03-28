package com.abc.uchat.homepage.Chats

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.abc.uchat.model.Channel

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class ChatsViewModel  @Inject constructor(): ViewModel()   {
    val firebaseDb = Firebase.database
    val channels = MutableStateFlow<List<Channel>>(emptyList())
    val _filteredChannels = MutableStateFlow<List<Channel>>(emptyList())
    var iscontain:Boolean = false

    init {
        getchannels()
    }

    private fun getchannels(){
        firebaseDb.getReference("channel").get()
            .addOnSuccessListener {
                val list = mutableListOf<Channel>()
                it.children.forEach{
                    data->
                    val channel =  Channel(data.key!! , data.value.toString())
                    list.add(channel)
                }
                channels.value = list
            }
    }


    fun getChannel(name: String) {
        firebaseDb.getReference("channel").get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.children.mapNotNull { data ->
                    if (data.value.toString() == name) {
                        data.key?.let { key -> Channel(key, data.value.toString()) }
                    } else null
                }
                _filteredChannels.value = list
            }
    }




    fun addchannel(name:String){
        val key = firebaseDb.getReference("channel").push().key
        firebaseDb.getReference("channel").child(key!!).setValue(name).addOnSuccessListener {
            getchannels()
        }
    }


}