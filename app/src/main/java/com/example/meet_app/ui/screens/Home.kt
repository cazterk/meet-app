package com.example.meet_app.ui.screens

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.meet_app.R
import com.example.meet_app.api.user.UserEntity
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.theme.fonts
import com.example.meet_app.ui.theme.getFonts
import com.example.meet_app.util.PullRefresh
import com.example.meet_app.viewmodel.AuthViewModel
import com.example.meet_app.viewmodel.UserViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),

    ) {
    var text by remember {
        mutableStateOf("")
    }
    var fonts = getFonts()
    val currentUser by userViewModel.currentUser.observeAsState()
    var name = "${currentUser?.firstName} ${currentUser?.lastName}"
    var isVisibilityEnabled by remember { mutableStateOf(false) }
    var discoveringStatus by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var refreshing by remember { mutableStateOf(false) }


    LaunchedEffect(viewModel, userViewModel, context, refreshing) {
        userViewModel.loadCurrentUser()
        Log.d(TAG, "refreshing state: $refreshing")
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    Toast.makeText(
                        context,
                        "Authorized",
                        Toast.LENGTH_LONG
                    ).show()


                }

                is AuthResult.Unauthorized -> {
                    navController.navigate(Screen.Login.withArgs("login")) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }

                }

                is AuthResult.UnknownError -> {
                    // Handle the unknown error case here
                    // You can show an error message, log the error, or take appropriate action
                }
            }
        }

    }
    PullRefresh(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            GlobalScope.launch {
                delay(1500)
                refreshing = false
            }
        }
    ) {

        Column(
            modifier = Modifier
                .background(color = Color.Transparent)


        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .height(100.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),


                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        modifier = Modifier
                            .size(65.dp)
                            .clip(shape = CircleShape)
                            .clickable {
                                navController.navigate(Screen.Profile.route)
                            },
                        painter = (if (currentUser?.profileImage != null) {
                            rememberAsyncImagePainter(currentUser?.profileImage)
                        } else {
                            painterResource(R.drawable.profile_image_placeholder)
                        }),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Image"
                    )
                    Column(
                        modifier = Modifier
                            .weight(weight = 3f, fill = false)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Welcome $name!",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = fonts,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Get started with connections",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Gray,
                                letterSpacing = (0.1).sp,
                                fontFamily = fonts,
                                fontWeight = FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }


                }
            }


            //  main options

            Card(

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Visibility",
                            fontSize = 13.sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            modifier = Modifier.scale(scale = 1.2f),

                            checked = isVisibilityEnabled,
                            onCheckedChange = { isChecked ->
                                isVisibilityEnabled = isChecked
                                if (isChecked) {
                                    userViewModel.startAdvertising()
                                } else {
                                    userViewModel.stopAdvertising()
                                }
                            },

                            )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Near By People",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Connections()
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            modifier = Modifier.width(150.dp),
                            onClick = {
                                navController.navigate(Screen.Messages.route)
                            })
                        {
                            Text(text = "Message")
                        }

                    }

                }

            }

        }
    }

}


@Composable
fun Connections(userView: UserViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {

    }
    val connections = userView.discoveredUsers


    Column {

        if (connections.isNotEmpty())
            LazyColumn {
                items(connections) { connection ->
                    ConnectionItem(connection = connection)

                }
            }
        else {
            Text(text = "No Possible Connections Nearby")
        }

    }
}

@Composable
fun ConnectionItem(connection: UserEntity) {
    val date = "01/01/2021"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            connection.profileImage?.let {
                Image(
                    modifier = Modifier
                        .size(65.dp)
                        .clip(shape = CircleShape),
                    painter = (if (connection?.profileImage != null) {
                        rememberAsyncImagePainter(it)
                    } else {
                        painterResource(R.drawable.profile_image_placeholder)
                    }),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Image"

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(weight = 3f, fill = false)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "${connection.firstName} ${connection.lastName}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.SemiBold,

                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Date Connected: $date",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Gray,
                            letterSpacing = (0.1).sp,
                            fontFamily = fonts,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }

        }


    }
}


