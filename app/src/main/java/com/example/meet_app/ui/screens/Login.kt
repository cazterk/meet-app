package com.example.meet_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.meet_app.R


//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController: NavController, name: String?) {

    var username by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isVisible by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember {
        FocusRequester()
    }
//    val keyboardController = LocalSoftwareKeyboardController.current

    val showProgress: Boolean by remember {
        mutableStateOf(false)
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)

    ) {
        val (
            logo, usernameTextField, passwordTextField, btnLogin, registerLink, progressBar
        ) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, margin = 30.dp)
                }
        )

        OutlinedTextField(
            value = username,
            label = { Text("Number or Username") },
            onValueChange = { newValue -> username = newValue },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(usernameTextField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logo.bottom, margin = 20.dp)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,

                ),
        )


        OutlinedTextField(
            value = password,
            label = { Text("Enter Password") },
            onValueChange = { newValue -> password = newValue },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(passwordTextField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(usernameTextField.bottom, margin = 20.dp)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,

                ),
            visualTransformation = if (isVisible) {
                VisualTransformation.None

            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(onClick = { isVisible = !isVisible }) {
                    Icon(
                        imageVector = if (isVisible) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }, contentDescription = null
                    )
                }
            }

        )


        Button(
            onClick = {
//                viewModel.loginUser(username.text,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiY2F6dGVyayJ9.mUqxgxQtLkdE4kEEAjTdOAbTNEKw6LdSIfyUgdzHh-8")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .constrainAs(btnLogin) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(passwordTextField.bottom, margin = 20.dp)
                },

            )
        {
            Text("Login")
        }

        Text(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(registerLink) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btnLogin.bottom, margin = 20.dp)
                },
            text = "Click here for new Users",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(
                fontSize = 15.sp
            )
        )
        if (showProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(registerLink.bottom, margin = 20.dp)
                    }
            )
        }


    }

}


//







