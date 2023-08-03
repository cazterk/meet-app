package com.example.meet_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Stop
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.meet_app.R
import com.example.meet_app.api.user.UserEntity
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.theme.fonts
import com.example.meet_app.ui.theme.getFonts
import com.example.meet_app.viewmodel.AuthViewModel
import com.example.meet_app.viewmodel.UserViewModel


@Composable
fun Home(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()

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
    LaunchedEffect(viewModel, userViewModel, context) {
        userViewModel.loadCurrentUser()
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
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent)

    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .padding(16.dp)
                .height(100.dp),

//            shape = RoundedCornerShape(10.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),


                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(shape = CircleShape)
                        .clickable {
                            navController.navigate(Screen.Profile.withArgs("profile"))
                        },
                    painter = painterResource(id = R.drawable.profile),
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
        Card() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
//                ConnectionsOptions()
                Connections()
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            navController.navigate(Screen.Messages.withArgs(text))
                        })
                    {
                        Text(text = "Message")
                    }
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(onClick = {
                                userViewModel.discoveringStatus(discoveringStatus)
                                discoveringStatus = !discoveringStatus

                            }) {
                                if (discoveringStatus){
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search"
                                )
                                }
                                else{
                                    Icon(
                                        imageVector = Icons.Outlined.Stop,
                                        contentDescription = "Stop"
                                    )
                                }
                            }
                        }
                    ) {

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
    val connections = userView.users


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
fun ConnectionItem(connection : UserEntity){

    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = "Username: ${connection.username}",
            style = MaterialTheme.typography.h6)
        Text(text = "Full Name: ${connection.firstName} ${connection.lastName}",
        style = MaterialTheme.typography.body1)

    }
}


@Composable
fun ConnectionsOptions(userViewModel: UserViewModel = hiltViewModel()) {
//    val users by userViewModel.users.observeAsState(emptyList())
    val discoveredUsers = userViewModel.discoveredUsers

//    val listOfConnectionsData = listOf(
////        UserResponse("", ""),
//        connectionsData(
//            "Martin James",
//            painterResource(R.drawable.person2),
//            "01/01/2022"
//        ),
//        connectionsData(
//            " Smith Daniel",
//            painterResource(R.drawable.person3),
//            "01/01/2022"
//        ),
//        connectionsData(
//            " Annie Daniel",
//            painterResource(R.drawable.person4),
//            "01/01/2021"
//        )
//    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        LazyColumn {
            items(discoveredUsers.size) { user ->
                ConnectionsListItems(user as UserEntity) { endpointId ->
                    userViewModel.shareUser(endpointId)

                }
            }
        }
    }
}

@Composable
fun ConnectionsListItems(user: UserEntity, onShareClick: (String) -> Unit) {
    val date = "01/01/2021"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            user.profileImage?.let {
//                Image(
//                    modifier = Modifier
//                        .size(50.dp)
//                        .clip(shape = CircleShape),
//                    painter = it,
//                    contentDescription = ""
//
//                )
//            }
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
                        text = " $user.firstName $user.lastName",
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


data class connectionsData(val fullName: String, val image: Painter, val date: String)
