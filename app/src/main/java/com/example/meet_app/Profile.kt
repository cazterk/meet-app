package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun Profile(navController: NavController, name: String?){
        val text by remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,


        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("This is the profile screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.width(150.dp),
            onClick = {
            navController.navigateUp()
        })
        {
            Text("Home")
        }
    }
}