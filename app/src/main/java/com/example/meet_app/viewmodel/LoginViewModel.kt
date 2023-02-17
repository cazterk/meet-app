package com.example.meet_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_app.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val client: ChatClient
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private fun isValidUsername(username: String): Boolean {
        return username.length > Constants.MIN_USERNAME_LENGTH
    }

    fun loginUser(username: String, token: String) {
        val trimmedUsername = username.trim()
        viewModelScope.launch {
            if (isValidUsername(trimmedUsername) && token != null) {
                loginRegistered(trimmedUsername, token)
            } else {
                _loginEvent.emit(LoginEvent.ErrorInputTooShort)
            }
        }
    }

    private fun loginRegistered(username: String, token: String) {

        val user = User(id = username, name = username)
        client.connectUser(
            user = user,
            token = token
        ).enqueue { result ->
            if (result.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LoginEvent.Success)
                }
            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LoginEvent.ErrorLogin(
                            result.error().message ?: "unknown error"
                        )
                    )
                }
            }
        }

    }

    sealed class LoginEvent {
        object ErrorInputTooShort : LoginEvent()
        data class ErrorLogin(val error: String) : LoginEvent()
        object Success : LoginEvent()
    }
}