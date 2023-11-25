package com.example.meet_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.meet_app.navigation.Screen

@Composable
fun Messages(navController: NavController, name: String?){

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("This is the Messages Screen")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
            navController.navigate(Screen.Home.route)
            }
        ) {
            Text("Go to Home")

        }
    }
}






