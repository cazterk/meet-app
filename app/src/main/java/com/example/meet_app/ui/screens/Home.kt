package com.example.meet_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.theme.fonts
import com.example.meet_app.ui.theme.getFonts
import com.example.meet_app.viewmodel.AuthViewModel


@Composable
fun Home(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var text by remember {
        mutableStateOf("")
    }
    var fonts = getFonts()
    var name = "Cephas"
    val shouldNavigate = remember { mutableStateOf(false) }


    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
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
                        .clip(shape = CircleShape),
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
                Text(

                    text = "Near By People",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                connectionsOptions()
                Spacer(modifier = Modifier.height(16.dp))
                Column() {
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            navController.navigate(Screen.Profile.withArgs(text))
                        })

                    {
                        Text("Profile")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            navController.navigate(Screen.Login.withArgs(text))
                        })
                    {
                        Text(text = "Login")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            navController.navigate(Screen.Messages.withArgs(text))
                        })
                    {
                        Text(text = "Message")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = {
                            navController.navigate(Screen.Register.withArgs(text))
                        })
                    {
                        Text(text = "Register")
                    }
                }

            }

        }

    }

}

@Composable
private fun connectionsOptions() {
    val listOfConnectionsData = listOf(
        connectionsData("John Doe", painterResource(R.drawable.person1), "01/01/2022"),
        connectionsData(
            "Martin James",
            painterResource(R.drawable.person2),
            "01/01/2022"
        ),
        connectionsData(
            " Smith Daniel",
            painterResource(R.drawable.person3),
            "01/01/2022"
        ),
        connectionsData(
            " Annie Daniel",
            painterResource(R.drawable.person4),
            "01/01/2021"
        )
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        LazyColumn {
            items(listOfConnectionsData.size) { index ->
                connectionsListItems(listOfConnectionsData[index])
            }
        }
    }
}

@Composable
fun connectionsListItems(connectionsData: connectionsData) {
    val date = connectionsData.date
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
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = CircleShape),
                painter = connectionsData.image,
                contentDescription = ""

            )
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
                        text = connectionsData.fullName,
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