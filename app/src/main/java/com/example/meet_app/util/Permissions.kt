package com.example.meet_app.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.meet_app.navigation.Navigation
import com.example.meet_app.ui.theme.fonts
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMultiplepermissions(
    navController: NavHostController,
    permissions: List<String>,
    deniedMessage: String = "Give this app permission, you can do it manually as well in your app settings",
    rationalMessage: String = "To use this app's functionalities, you must give this app permission!"
) {
    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    HandleRequests(
        multiplePermissionsState = multiplePermissionsState,
        deniedContent = { shouldShowRationale ->
            permissionDeniedContent(
                deniedMessage = deniedMessage,
                rationalMessage = rationalMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() }

            )
        },
        content = {

            Navigation(navController = navController)
        }
    )

}


@ExperimentalPermissionsApi
@Composable
private fun HandleRequests(
    multiplePermissionsState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val result = multiplePermissionsState.permissions.all {
        shouldShowRationale = it.status.shouldShowRationale
        it.status == PermissionStatus.Granted
    }
    if (result) {
        content()
    } else {
        deniedContent(shouldShowRationale)
    }
}

@ExperimentalPermissionsApi
@Composable
fun permissionDeniedContent(
    deniedMessage: String,
    rationalMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontFamily = fonts,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                Text(rationalMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )
    } else {

        Content(text = deniedMessage, onClick = onRequestPermission)
    }
}

@Composable
fun Content(text: String, showButton: Boolean = true, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(12.dp))
        if (showButton) {
            Button(onClick = onClick) {
                Text(text = "Request")
            }
        }
    }
}
