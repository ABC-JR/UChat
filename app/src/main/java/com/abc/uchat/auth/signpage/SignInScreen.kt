package com.abc.uchat.auth.signpage
import android.annotation.SuppressLint

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abc.uchat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInscreen(navController: NavController ) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var viewModel:SignInViewModel = hiltViewModel()
    var stateui = viewModel.state.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize().padding(5.dp))
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
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(value = password,
                    onValueChange = { password = it }  ,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation() ,
                    label = { Text(text = "Password") }
                )

                if(stateui.value == SignInState.Loading){
                    CircularProgressIndicator()
                }else{
                    Button(onClick = {
                        viewModel.signIn(login,password)


                    } ,
                        enabled = login.isNotEmpty() && password.isNotEmpty() && (stateui.value == SignInState.Nothing || stateui.value == SignInState.Error)
                    ) {
                        Text(text = "Sign in")
                    }
                }

                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Don't have account create new account" , modifier = Modifier.padding(5.dp).clickable {
                    navController.navigate("signup")

                })


            }
        }

    }


}



