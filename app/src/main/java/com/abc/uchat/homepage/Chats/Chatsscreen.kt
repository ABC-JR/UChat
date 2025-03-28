package com.abc.uchat.homepage.Chats

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import com.abc.uchat.model.Channel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Chats(navController: NavHostController) {
    val viewModel = hiltViewModel<ChatsViewModel>()
    val channels = viewModel.channels.collectAsState()
    val filteredChannels = viewModel._filteredChannels.collectAsState()
    var query by remember { mutableStateOf("") }
    val isSearching = remember { mutableStateOf(false) }
    val dialogState = remember { mutableStateOf(false) }


    val addchannel =remember{
        mutableStateOf(false)
    }

    var history = remember { mutableStateOf(listOf<Channel>()) }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        topBar = {

            MyTopAppbar()
        },
        bottomBar = {
            MyBottomNavigation(navController)
        }  ,
        floatingActionButton = {
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue)
                .clickable {
                    addchannel.value = true
                })

            {
                Text(text = "Add channel" ,
                    modifier = Modifier.padding(5.dp)
                         , color = Color.White)
            }

        }
    ) {paddingval->

        Column (modifier = Modifier.padding(paddingval)){
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = { Text(text = "Searching...") },
                active = isSearching.value,
                onActiveChange = { isSearching.value = it },
                onSearch = {
                    viewModel.getChannel(it)
                    isSearching.value = false
                    dialogState.value = filteredChannels.value.isNotEmpty()
                }
            ) {
                if (dialogState.value) {
                    body(filteredChannels, navController)

                    history.value =  history.value  + filteredChannels.value
                }else{
                    body(history, navController)
                }


            }

            body(channels, navController)
        }



    }
    if(addchannel.value){
        ModalBottomSheet(onDismissRequest = {
            addchannel.value = false
        } , sheetState = sheetState) {
            addchanneldialog{
                viewModel.addchannel(it)
                addchannel.value = false
            }

        }
    }else{

    }
}






@Composable
fun body(channels: State<List<Channel>> , navController: NavHostController) {
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        items(channels.value) {
            channels->
            Spacer(modifier = Modifier.padding(2.dp))
            people(channels , navController = navController)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .alpha(0.4f)
                .background(color = Color.Black)
            )
            Spacer(modifier = Modifier.padding(2.dp))
        }
    }
}





@Composable
fun people(channel: Channel, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(6.dp)  // Добавил отступы
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = {
            navController.navigate("onechat/${channel.id}/${channel.name}")
        }
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Face,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .padding(end = 8.dp), // Отступ между иконкой и текстом
                tint = Color.DarkGray
            )
            Column {
                Text(
                    text = channel.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Последнее сообщение...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val listOfBottom = listOf(
        BottomItem("Chats", Icons.Default.Send, "chats"),
        BottomItem("Settings", Icons.Default.Settings, "settings")
    )

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray,
        modifier = Modifier.border(1.dp, color = Color.LightGray)
    ) {
        listOfBottom.forEach { nav ->
            NavigationBarItem(
                selected = nav.route == currentRoute,
                onClick = {
                    navController.navigate(nav.route)
                },
                icon = {
                    Icon(
                        imageVector = nav.img,
                        contentDescription = "Icon for ${nav.name}",
                        modifier = Modifier.size(28.dp),
                        tint = if (nav.route == currentRoute) Color.Blue else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = nav.name,
                        fontSize = 14.sp,
                        fontWeight = if (nav.route == currentRoute) FontWeight.Bold else FontWeight.Normal,
                        color = if (nav.route == currentRoute) Color.Blue else Color.Gray
                    )
                }
            )
        }
    }
}

data class BottomItem(val name: String, val img: ImageVector, val route: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppbar(){
    TopAppBar(title = {
        Column(
            modifier = Modifier.fillMaxSize() ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Uchat" , fontSize = 25.sp , fontWeight = FontWeight.W500  )
            Box(modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(color = Color.Gray)
            )
        }
    }
    )

}



@Composable
fun addchanneldialog(onAddChannel:(String) ->Unit){
    val channel = remember{
        mutableStateOf("")
    }
    Column (verticalArrangement = Arrangement.Center , horizontalAlignment = Alignment.CenterHorizontally){

        Spacer(modifier = Modifier.padding(3.dp))
        TextField(value =channel.value  , onValueChange =  {channel.value = it} , label = {
            Text(text = "Channal name")
        } ,
            singleLine = true)
        Spacer(modifier = Modifier.padding(3.dp))
        Button(onClick = {
            onAddChannel(channel.value)
        }) {
            Text(text = "Add")
        }

    }
}


@Preview(showBackground = true)
@Composable
fun main56(){
    Chats(rememberNavController())
}