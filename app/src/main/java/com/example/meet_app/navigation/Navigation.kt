package com.example.meet_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.screens.*

@Composable
fun Navigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // login
        composable(
            route = Screen.Login.route + "/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Login(navController = navController, name = entry.arguments?.getString("name"))
        }
        // register
        composable(
            route = Screen.Register.route + "/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Register(navController = navController, name = entry.arguments?.getString("name"))
        }
// home
        composable(
            route = Screen.Home.route
        ) {
            Home(navController = navController)


        }
        // profile
        composable(
            route = Screen.Profile.route + "/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "John"
                    nullable = true
                }
            )
        ) { entry ->
            Profile(navController = navController, name = entry.arguments?.getString("name"))
        }
        // messages
        composable(
            route = Screen.Messages.route + "/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Messages(navController = navController, name = entry.arguments?.getString("name"))
        }


    }
}