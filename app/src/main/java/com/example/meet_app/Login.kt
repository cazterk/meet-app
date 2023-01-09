package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Login(navController:NavController, name: String?){
    var emailUsernameFieldState by remember {
        mutableStateOf("")
    }
    var passwordFieldState by remember{
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Welome To MeetApp")
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = emailUsernameFieldState,
            label = {
                Text("Email or Username")
                    },
            onValueChange ={
                emailUsernameFieldState = it
            },
            singleLine = true,
            modifier = Modifier
                .width(360.dp)
//                .height(40.dp)
            )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = passwordFieldState,
            label = {
                Text("Password")},
            onValueChange ={
                passwordFieldState = it
            },
            placeholder = { "Enter your password" },
            singleLine = true,
            modifier = Modifier
                .width(360.dp)
//                .height(40.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .width(350.dp)
                .height(50.dp),
            onClick = {
                navController.navigateUp()
            })
        {
            Text("Home")
        }
    }
}