package com.example.meet_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.meet_app.R
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.auth.AuthUIEvent
import com.example.meet_app.viewmodel.AuthViewModel


//@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(
    navController: NavController,
    name: String?,
    viewModel: AuthViewModel = hiltViewModel()
) {

    var text by remember {
        mutableStateOf("")
    }
    val state = viewModel.state
    val context = LocalContext.current
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthorized -> {

                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        "${result.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    println(result.message)
                }
            }
        }
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
            value = state.signInUsername,
            label = { Text("Number or Username") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignInUsernameChanged(it))
            },
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
            value = state.signInPassword,
            label = { Text("Enter Password") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignInPasswordChanged(it))
            },
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
                viewModel.onEvent(AuthUIEvent.SignIn)
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
                .constrainAs(registerLink) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btnLogin.bottom, margin = 20.dp)
                }
                .clickable {
                    navController.navigate(Screen.Register.withArgs("register"))
                },
            text = "Click here for new Users",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(
                fontSize = 15.sp
            )
        )
        if (state.isLoading) {
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







