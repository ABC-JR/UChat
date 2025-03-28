package com.abc.uchat.homepage.onechats

import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Nickname
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.abc.uchat.R
import com.abc.uchat.ui.theme.coloroftop

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun onechat(navcontroller: NavController, channelid: String, nickname: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navcontroller.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                title = {
                    Text(text = nickname, fontWeight = FontWeight.W600, color = Color.Black)
                },
                modifier = Modifier.background(coloroftop)
            )
        }
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize().padding(it)) {
            val viewmodel = hiltViewModel<onechatviewmodel>()
            LaunchedEffect(true) {
                viewmodel.listenForMessages(channelid)
            }
            val messages = viewmodel.messages.collectAsState()
            ChatMessages(
                messages = messages.value,
                onSendMessage = { message ->
                    viewmodel.sendmesseage(channelid, message)
                }
            )
        }
    }
}



@Composable
fun ChatMessages(
    messages: List<Messages>,
    onSendMessage: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var msg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            items(messages) {
                ChatBubble(it)
                Spacer(modifier = Modifier.height(6.dp)) // Отступ между сообщениями
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = msg,
                onValueChange = { msg = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                placeholder = { Text(text = "Type message...") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (msg.isNotBlank()) {
                            onSendMessage(msg)
                            msg = ""
                            keyboardController?.hide()
                        }
                    }
                )
            )

            IconButton(
                onClick = {
                    if (msg.isNotBlank()) {
                        onSendMessage(msg)
                        msg = ""
                        keyboardController?.hide()
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Blue)
            }
        }
    }
}


@Composable
fun ChatBubble(message: Messages) {
    val iscurrent= message.senderId == Firebase.auth.currentUser?.uid
    val color = if(iscurrent){
        Color.Gray
    }else{
        Color.Blue  
    }
    Row(modifier = Modifier.fillMaxWidth()
        .padding(5.dp)  ,
     horizontalArrangement = Arrangement.spacedBy(8.dp) ,
        verticalAlignment = Alignment.Bottom
    ){

        var alignment:Alignment
        if(iscurrent){
             alignment = Alignment.CenterEnd
        }else{
            alignment = Alignment.CenterStart
        }
        Box(
            contentAlignment = alignment,
            modifier = Modifier.
            wrapContentSize()
                .background(color , shape = RoundedCornerShape(5.dp)),
        ){
            Text(text = message.messages , color= Color.White)
        }
    }
}

