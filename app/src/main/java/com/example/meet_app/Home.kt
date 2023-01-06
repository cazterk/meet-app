package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController){
    var text by remember {
        mutableStateOf("")
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Text("this is the main screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
                navController.navigate(Screen.Profile.withArgs(text))
            })

        {
            Text("Profile")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
                navController.navigate(Screen.Login.withArgs(text))
            })
        {
             Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
                navController.navigate(Screen.Message.withArgs(text))
            })
        {
            Text(text = "Message")
        }
    }

}