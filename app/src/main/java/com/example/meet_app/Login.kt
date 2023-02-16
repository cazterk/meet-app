package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController:NavController, name: String?){
    var emailUsernameFieldState by remember {
        mutableStateOf("")
    }
    var passwordFieldState by remember {
        mutableStateOf("")
    }
    var isVisible by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember {
        FocusRequester()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)

    ){
        val (
        logo, usernameTextField, btnLogin, progressBar
        ) = createRefs()

//        Image(
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = "Logo",
//            modifier = Modifier
//                .height(120.dp)
//                .width(120.dp)
//                .constrainAs(logo){
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    top.
//                }
//        )
    }

//    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(40.dp)
//    ) {
//        Text(
//            text = "MeetApp",
//            fontWeight = FontWeight.SemiBold,
//            style= TextStyle(
//                fontSize = 25.sp
//            )
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        OutlinedTextField(
//            value = emailUsernameFieldState,
//            label = {
//                Text("Email or Username")
//                    },
//            onValueChange ={
//                emailUsernameFieldState = it
//            },
//            singleLine = true,
//            modifier = Modifier
//                .fillMaxWidth(),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Text,
//                imeAction = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions(
//                onNext = {
//                    focusRequester.requestFocus()
//                }
//            )
//
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        OutlinedTextField(
//            value = passwordFieldState,
//            label = {
//                Text("Password")},
//            onValueChange ={
//                passwordFieldState = it
//            },
//            placeholder = { "Enter your password" },
//            singleLine = true,
//            modifier = Modifier
//                .fillMaxWidth()
//                .focusRequester(focusRequester),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Password
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = {
//                    keyboardController?.hide()
//                }
//            ),
//            visualTransformation = if (isVisible){
//                    VisualTransformation.None
//
//            }else {
//                PasswordVisualTransformation()
//            },
//            trailingIcon =  {
//                IconButton(onClick = { isVisible = !isVisible}) {
//                    Icon(
//                        imageVector = if (isVisible){
//                        Icons.Filled.Visibility
//                        } else {
//                            Icons.Filled.VisibilityOff
//                        }, contentDescription = null
//                    )
//                }
//            }
//
//
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            enabled =
//                    emailUsernameFieldState.isNotEmpty()
//                    && passwordFieldState.isNotEmpty(),
//            onClick = {
//                navController.navigateUp()
//            })
//        {
//            Text("Login")
//        }
//    }
}