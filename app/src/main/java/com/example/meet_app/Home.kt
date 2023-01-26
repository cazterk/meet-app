package com.example.meet_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController){
    var text by remember {
        mutableStateOf("")
    }
    var fonts = getFonts()
    var name = "Cephas"
    var username = "@cazterk"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)

    ) {


    Column( 
        modifier = Modifier
            .padding(32.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(top = 38.dp)
                .background(color = Color.White),

            verticalAlignment = Alignment.CenterVertically
            ){
            Spacer(modifier = Modifier.width(16.dp))
              Image(
                  modifier = Modifier
                      .size(55.dp)
                      .clip(shape = CircleShape)
                      ,
                  painter = painterResource(id = R.drawable.profile) , 
                  contentDescription = "Image" 
              )
            Column(
                modifier = Modifier
                    .weight(weight = 3f, fill = false)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Welcome $name",
                    style =  TextStyle(
                        fontSize = 22.sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines= 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Get started with connections",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        letterSpacing = (0.8).sp,
                        fontFamily = fonts,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
            }
            
        }
        
    }
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    ) {
        Text("this is the main screen")
        Spacer(modifier = Modifier.height(16.dp))
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
                navController.navigate(Screen.Message.withArgs(text))
            })
        {
            Text(text = "Message")
        }
    }

  }
}