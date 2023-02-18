package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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
            navController.navigateUp()
            }
        ) {
            Text("Go to Home")

        }
    }
}






