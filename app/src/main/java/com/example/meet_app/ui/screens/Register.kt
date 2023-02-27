package com.example.meet_app.ui.screens


import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.meet_app.R
import com.example.meet_app.auth.AuthResult
import com.example.meet_app.navigation.Screen
import com.example.meet_app.ui.auth.AuthUIEvent
import com.example.meet_app.viewmodel.AuthViewModel


@Composable
fun Register(
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
                    navController.navigate(Screen.Home.withArgs(text)) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(
                        context,
                        "You are not authorized",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.UnknownError -> {
                    Toast.makeText(
                        context,
                        "An unknown error occurred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    var username by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var firstName by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var lastName by remember {
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
            logo,
            usernameTextField,
            firstNameTextField,
            lastNameTextField,
            passwordTextField,
            btnLogin,
            LoginLink,
            progressBar
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
                    top.linkTo(parent.top, margin = 20.dp)
                }
        )

        OutlinedTextField(
            value = state.signUpFirstName,
            label = { Text("First name") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignUpFirstNameChanged(it))
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(firstNameTextField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(logo.bottom, margin = 20.dp)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,

                ),
        )

        OutlinedTextField(
            value = state.signUpLastName,
            label = { Text("Last Name") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignUpLastNameChanged(it))
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(lastNameTextField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(firstNameTextField.bottom, margin = 20.dp)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,

                ),
        )

        OutlinedTextField(
            value = state.signUpUsername,
            label = { Text("Username") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignUpUsernameChanged(it))
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(usernameTextField) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(lastNameTextField.bottom, margin = 20.dp)
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,

                ),
        )

        OutlinedTextField(
            value = state.signUpPassword,
            label = { Text("Enter Password") },
            onValueChange = {
                viewModel.onEvent(AuthUIEvent.SignUpPasswordChanged(it))
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
                viewModel.onEvent(AuthUIEvent.SignUp)
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
            Text("Register")
        }

        Text(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(LoginLink) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(btnLogin.bottom, margin = 20.dp)
                },
            text = "Already have an account?",
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
                        top.linkTo(LoginLink.bottom, margin = 20.dp)
                    }
            )
        }


    }
}

