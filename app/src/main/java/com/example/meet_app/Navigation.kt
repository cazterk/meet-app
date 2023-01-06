package com.example.meet_app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route ){
        composable(route = Screen.Home.route){
            Home(navController = navController)
        }
    }
}