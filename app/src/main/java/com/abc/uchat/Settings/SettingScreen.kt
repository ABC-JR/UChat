package com.abc.uchat.Settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.abc.uchat.R
import com.abc.uchat.homepage.Chats.MyBottomNavigation
import com.abc.uchat.homepage.Chats.MyTopAppbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SettingScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            MyTopAppbar()
        },
        bottomBar = {
            MyBottomNavigation(navController)
        }
    ) { paddingValues ->
        var showdialog by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // User Avatar
            AsyncImage(
                model = "https://i.pinimg.com/originals/cd/88/61/cd8861090058c5f3f340fcf283c76cf8.jpg",
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Info
            val userName = Firebase.auth.currentUser?.displayName ?: "Unknown User"
            val email = Firebase.auth.currentUser?.email ?: "No Email"

            Text(text = userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileButton(icon = Icons.Default.Edit, text = "Edit") {
                    showdialog = true
                }
                ProfileButton(icon = Icons.Default.ExitToApp, text = "Quit") {
                    navController.navigate("signin")
                }
            }

            if(showdialog){
                EditProfileDialog {
                    showdialog = false
                }
            }
        }
    }
}



@Composable
fun EditProfileDialog(onDismiss: () -> Unit) {
    var nickname by remember { mutableStateOf(Firebase.auth.currentUser?.displayName ?: "") }
    var email by remember { mutableStateOf(Firebase.auth.currentUser?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Edit Profile") },
        text = {
            Column {
                TextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text(text = "Nickname") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "New Password") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    isLoading = true
                    val user = Firebase.auth.currentUser

                    val updates = user?.updateProfile(
                        com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(nickname)
                            .build()
                    )

                    val updateEmail = if (email.isNotEmpty() && email != user?.email) {
                        user?.updateEmail(email)
                    } else null

                    val updatePassword = if (password.isNotEmpty()) {
                        user?.updatePassword(password)
                    } else null

                    updates?.addOnCompleteListener {
                        updateEmail?.addOnCompleteListener {
                            updatePassword?.addOnCompleteListener {
                                isLoading = false
                                onDismiss()
                            }
                        } ?: run {
                            isLoading = false
                            onDismiss()
                        }
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}




@Composable
fun ProfileButton(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

