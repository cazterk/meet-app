package com.example.meet_app.activity

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.meet_app.util.RequestMultiplepermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            RequestMultiplepermissions(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.ACCESS_WIFI_STATE,
//                    Manifest.permission.BLUETOOTH_SCAN,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.NEARBY_WIFI_DEVICES,
                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_WIFI_STATE,
//                    Manifest.permission.CHANGE_WIFI_STATE,
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.BLUETOOTH_ADVERTISE,
//                    Manifest.permission.BLUETOOTH_CONNECT,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                navController = navController
            )

        }
    }


}

