package com.example.meet_app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.meet_app.ui.screens.Connections
import com.example.meet_app.ui.screens.Home
import com.example.meet_app.ui.screens.Login
import com.example.meet_app.ui.screens.Messages
import com.example.meet_app.ui.screens.Profile
import com.example.meet_app.ui.screens.Register


@Composable
fun Navigation(
    navController: NavHostController,
) {


    val routesToHideInBottomNavBar = listOf(
        Screen.Register.route,
        Screen.Login.route,
        Screen.Profile.route
    )
//    val currentBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry

    Scaffold(
        bottomBar = {
            BottomNavBar(
                navController = navController,
                routesToHideNav = routesToHideInBottomNavBar
            )
        }
    ) { it: PaddingValues -> it }
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
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("home") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            Home(navController = navController)


        }
        // profile
        composable(
            route = Screen.Profile.route,
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
            route = Screen.Messages.route,
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Messages(navController = navController, name = entry.arguments?.getString("name"))
        }

        // connections
        composable(
            route = Screen.Connections.route,
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { entry ->
            Connections(navController = navController, name = entry.arguments?.getString("name"))
        }


    }
}

