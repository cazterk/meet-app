package com.example.meet_app.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meet_app.util.RequestMultiplepermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            RequestMultiplepermissions(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                navController = navController
            )
//            val refreshScope = rememberCoroutineScope()
//            var refreshing by remember { mutableStateOf(false) }
//            var itemCount by remember { mutableStateOf(15) }
//
//            fun refresh() = refreshScope.launch {
//                refreshing = true
//                delay(1500)
//                itemCount = 1
//                refreshing = false
//            }
//
//            val state = rememberPullRefreshState(refreshing, ::refresh)

//            Box(Modifier.pullRefresh(state)) {
//                LazyColumn(Modifier.fillMaxSize()) {
//                    if (!refreshing) {
//                        items(itemCount) {
//                            ListItem {
//                                Text(text = "Item ${itemCount - it}")
//                            }
//                        }
//                    }
//                }
//            }

        }
    }


}

