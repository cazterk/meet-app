package com.example.meet_app.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_app.api.user.UserEntity
import com.example.meet_app.api.user.UserRepository
import com.example.meet_app.util.Constants.SERVICE_ID
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    application: Application

) : ViewModel() {
    private val _currentUser = MutableLiveData<UserEntity>()
    val currentUser: LiveData<UserEntity> = _currentUser

    private val _discoveredUsers = mutableStateListOf<UserEntity>()
    val discoveredUsers: List<UserEntity> get() = _discoveredUsers

    private val nearByShareClient = Nearby.getConnectionsClient(application)
    private val userToEndpointMap = HashMap<String, UserEntity>()

    private var payloadSent = false

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val response = userRepository.getCurrentUser()
                _currentUser.value = response
            } catch (e: Exception) {
                print("get user error" + e.message)
            }
        }
    }

    private fun parseUserProfile(userBytes: ByteArray): UserEntity {
        val inputStream = ByteArrayInputStream(userBytes)
        val objectInputStream = ObjectInputStream(inputStream)
        val user = objectInputStream.readObject() as UserEntity
        objectInputStream.close()
        inputStream.close()
        return user
    }

    fun receiveUserData(userJson: String) {
        val receivedUser = Gson().fromJson(userJson, UserEntity::class.java)
        _currentUser.value = receivedUser
    }

    inner class ConnectingProcessCallback : ConnectionLifecycleCallback() {
        private var strendPointId: String? = null
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            nearByShareClient.acceptConnection(endpointId, payloadCallback)


            sendPayloadToConnectedDevice(endpointId)


        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            viewModelScope.launch {
                val currentUser = userRepository.getCurrentUser()
                println(currentUser)

                if (result.status.isSuccess) {
                    Log.d(TAG, "Connection successful")
                } else {
                    Log.e(TAG, "Connection failed")
                }
            }
        }

        override fun onDisconnected(endpointId: String) {
            strendPointId = null
            Log.d(TAG, "Disconnected from endpoint $endpointId")
        }

    }


    private fun startDiscovery() {
        val discoveryOptions = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()

        nearByShareClient.startDiscovery(
            SERVICE_ID,
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(endpointId: String, ifon: DiscoveredEndpointInfo) {
                    nearByShareClient.requestConnection(
                        android.os.Build.MODEL,
                        endpointId,
                        ConnectingProcessCallback()
                    )
                }

                override fun onEndpointLost(p0: String) {
                    Log.d(TAG, "Connection lost")
                }


            }, discoveryOptions
        )
            .addOnSuccessListener {
                Log.d(TAG, "Discovery started")
            }
            .addOnFailureListener { exception ->
                // Handle the exception and show an error message
                Log.e(TAG, "Discovery failed: ${exception.message}")
            }

    }

    fun startAdvertising() {
        val advertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearByShareClient.startAdvertising(
            android.os.Build.MODEL, SERVICE_ID, ConnectingProcessCallback(), advertisingOptions
        )
            .addOnSuccessListener {
                Log.d(TAG, "Advertising started")
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Advertising failed: ${exception.message}")
            }
    }


    private fun stopDiscovery() {
        nearByShareClient.stopDiscovery()
        Log.d(TAG, "Discovery stopped")
    }

    fun stopAdvertising() {
        nearByShareClient.stopAdvertising()
        Log.d(TAG, "Advertising stopped")
    }

    fun discoveringStatus(status: Boolean) {
        if (status)
            startDiscovery()
        else stopDiscovery()

    }

    fun shareUser(endpointId: String) {
        val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
        nearByShareClient.sendPayload(endpointId, payload)

    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val userJson = payload.asBytes()?.toString(Charsets.UTF_8)
                userJson?.let { jsonString ->
                    val receivedUser = Gson().fromJson(jsonString, UserEntity::class.java)

                    // Check if the received user already exists in the list based on their ID
                    val existingUser = _discoveredUsers.find { it.id == receivedUser.id }
                    if (existingUser == null) {
                        _discoveredUsers.add(receivedUser)
                    }
                }
            }

            if (!payloadSent) {
                sendPayloadToConnectedDevice(endpointId)
                payloadSent = true
            }
//            val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
            Log.d(TAG, "Received payload from $endpointId: $payload")
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            if (update.status == PayloadTransferUpdate.Status.SUCCESS) {
                Log.d(TAG, "Payload sent to $endpointId successfully")
            }
        }
    }

    private fun sendPayloadToConnectedDevice(endpointId: String) {
        viewModelScope.launch {
            val currentUser = userRepository.getCurrentUser()
            val currentUserJson = Gson().toJson(currentUser)

            nearByShareClient.sendPayload(
                endpointId,
                Payload.fromBytes(currentUserJson.toByteArray())
            )
        }
    }

    fun removeDuplicatesUsersByID(users: MutableList<UserEntity>) {
        val uniqueUsers = LinkedHashMap<String, UserEntity>()

        for (user in users) {
            uniqueUsers[user.id] = user
        }

        users.clear()
        users.addAll(uniqueUsers.values)
    }
}
