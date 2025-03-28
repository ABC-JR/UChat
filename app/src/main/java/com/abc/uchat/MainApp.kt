package com.abc.uchat

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.abc.uchat.Settings.SettingScreen
import com.abc.uchat.auth.signpage.SignInscreen
import com.abc.uchat.auth.signuppage.Signupscreen
import com.abc.uchat.homepage.Chats.Chats
import com.abc.uchat.homepage.onechats.onechat

import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainApp(){
    Scaffold (modifier = Modifier.fillMaxSize()){
        var navcontroller = rememberNavController()
        val currentuser = FirebaseAuth.getInstance().currentUser
        var start= ""
        if(currentuser != null){
            start = "chats"
        }else{
            start = "signin"
        }
        NavHost(navController = navcontroller , startDestination = start){
            composable("signin") {
                SignInscreen(navcontroller )
            }
            composable("signup"){
                Signupscreen(navcontroller)
            }
            composable("chats") {
                Chats(navcontroller)
            }
            composable("settings") {
                SettingScreen(navcontroller)
            }
            composable("onechat/{channelid}/{channelname}") {
                val channelid= it.arguments?.getString("channelid") ?:""
                val nickname =  it.arguments?.getString("channelname")?:""
                onechat(navcontroller , channelid , nickname)
            }


        }
    }
}






