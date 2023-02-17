package com.example.meet_app.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient

@HiltViewModel
class LoginViewModel constructor(
    private val client: ChatClient
): ViewModel() {
}