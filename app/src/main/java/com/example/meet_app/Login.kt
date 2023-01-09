package com.example.meet_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
    ) {
        Text(
            text = "MeetApp",
            fontWeight = FontWeight.SemiBold,
            style= TextStyle(
                fontSize = 25.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = emailUsernameFieldState,
            label = {
                Text("Email or Username")
                    },
            onValueChange ={
                emailUsernameFieldState = it
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester.requestFocus()
                }
            )

        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = passwordFieldState,
            label = {
                Text("Password")},
            onValueChange ={
                passwordFieldState = it
            },
            placeholder = { "Enter your password" },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            visualTransformation = if (isVisible){
                    VisualTransformation.None

            }else {
                PasswordVisualTransformation()
            },
            trailingIcon =  {
                IconButton(onClick = { isVisible = !isVisible}) {
                    Icon(
                        imageVector = if (isVisible){
                        Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }, contentDescription = null
                    )
                }
            }


        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled =
                    emailUsernameFieldState.isNotEmpty()
                    && passwordFieldState.isNotEmpty(),
            onClick = {
                navController.navigateUp()
            })
        {
            Text("Login")
        }
    }
}