package com.example.meet_app.navigation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BadgedBox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.meet_app.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(
    navController: NavController,
    routesToHideNav: List<String>,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    val items = listOf(
        BottomNavigationItem(
            title = "home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false

        ),
        BottomNavigationItem(
            title = "messages",
            selectedIcon = Icons.Filled.ChatBubble,
            unselectedIcon = Icons.Outlined.ChatBubbleOutline,
            hasNews = false,
            badgeCount = 4,
            args = Screen.Messages
        ),
        BottomNavigationItem(
            title = "connections",
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.PeopleOutline,
            hasNews = true,
            args = Screen.Connections
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isHomeScreen = currentRoute == Screen.Home.route
    val shouldHideItem = !isHomeScreen && currentRoute in routesToHideNav
    var discoveringStatus by remember { mutableStateOf(true) }

    LaunchedEffect(currentRoute) {
        Log.d(TAG, "current route : $currentRoute")
    }


    if (!shouldHideItem)
        Scaffold(
            floatingActionButton = {
                if (isHomeScreen)
                    FloatingActionButton(
                        onClick = {
                    userViewModel.discoveringStatus(discoveringStatus)
                        discoveringStatus = !discoveringStatus

                    }
                    ,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (discoveringStatus) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Stop,
                                contentDescription = "Stop"
                            )
                        }
                    }
            },
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                if (item.args !== null) {
                                    navController.navigate(item.title)
                                    Log.d(TAG, "current route : $currentRoute")
                                } else navController.navigate(item.title)
                            }, label = {
                                Text(text = item.title)
                            },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if (item.hasNews) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            })
                    }
                }
            }
        ) {

        }


}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val args: Screen? = null,
)