package com.abc.uchat.auth.signuppage



import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abc.uchat.R
import com.abc.uchat.homepage.Chats.ChatsViewModel
import kotlin.math.log

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Signupscreen(navcontroller: NavHostController) {


    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nickname by remember { mutableStateOf("") }
    val viewmodelofchat = hiltViewModel<ChatsViewModel>()

    val viewModel:SignUpViewModel = hiltViewModel()


    Scaffold(modifier = Modifier.fillMaxSize().padding(5.dp) )
    {
        Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center){
            Column(
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(R.drawable.logouchat),
                    "logo" ,
                    modifier = Modifier.size(150.dp))
                OutlinedTextField(value = login,
                    onValueChange = { login = it } ,
                    label = { Text(text = "Login") }
                )
                OutlinedTextField(value = nickname,
                    onValueChange = { nickname = it } ,
                    label = { Text(text = "Nickname") }
                )
                OutlinedTextField(value = password,
                    onValueChange = { password = it }  ,
                    visualTransformation = PasswordVisualTransformation() ,
                    label = { Text(text = "Password") }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Button(onClick = {
                    viewModel.signup(nickname , login  , password)
                    nickname =""
                    login=""
                    password= ""
                    viewmodelofchat.addchannel(nickname )
                    navcontroller.navigate("chats")
                }) {
                    Text(text = "Sign Up")
                }

                Spacer(modifier = Modifier.padding(5.dp))
                TextButton (onClick = {navcontroller.popBackStack()}){
                    Text(text = "Already have account" , modifier = Modifier.padding(5.dp))
                }


            }
        }

    }

}
@Preview(showBackground = true)
@Composable
fun main22(){
    Signupscreen(
        navcontroller =rememberNavController()
    )
}
