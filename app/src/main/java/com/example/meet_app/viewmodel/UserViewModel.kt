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
import org.json.JSONObject
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

    private val _users = mutableStateListOf<UserEntity>()
    val users: List<UserEntity> get() = _users

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

    fun insertCurrentUser() {
        viewModelScope.launch {
            try {
                userRepository.insertUser()
            } catch (e: Exception) {
                print("insert user error" + e.message)
            }
        }
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            try {
                userRepository.deleteUser()
                Log.d(TAG, "delete user")
            } catch (e: Exception) {
                print("insert user error" + e.message)
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


//    private fun getEndpointUser(endpoint: String): UserResponse? {
//        val users = users.value ?: emptyList()
//
//        for (user in users) {
//             endpoint == user.endpointId
//                return user
//
//        }
//        return null
//    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val userJson = payload.asBytes()?.toString(Charsets.UTF_8)
                userJson?.let {
                    val receivedUser = Gson().fromJson(it, UserEntity::class.java)


                    _discoveredUsers.add(receivedUser)

                }
            }

            if (!payloadSent) {
                // Fire your method or code block
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

fun removeDuplicatesUsersByID(users: MutableList<UserEntity>){
    val uniqueUsers = LinkedHashMap<String, UserEntity>()

    for (user in users) {
    uniqueUsers[user.id] = user
    }

    users.clear()
    users.addAll(uniqueUsers.values)
}

    private fun serializeUser(user: UserEntity): ByteArray {
        val data = JSONObject()
        data.put("id", user.id)
        data.put("username", user.username)
        data.put("firstName", user.firstName)
        data.put("lastName", user.lastName)
        return data.toString().toByteArray(Charsets.UTF_8)
    }

//    private fun deserializeUser(bytes: ByteArray): UserEntity {
//        val jsonString = String(bytes, Charsets.UTF_8)
//        val jsonObject = JSONObject(jsonString)
//        return UserEntity(
//            id = jsonObject.getString("id"),
//            username = jsonObject.getString("username"),
//            firstName = jsonObject.getString("firstName"),
//            lastName = jsonObject.getString("lastName"),
//        )
//    }
//companion object
//    fun UserResponse.fromPayload(payload: Payload): UserResponse {
//        val data = payload.asBytes()?.toString(Charsets.UTF_8) ?: ""
//        val jsonObject = JSONObject(data)
//        return UserResponse(
//            jsonObject.getString("id"),
//            jsonObject.getString("username"),
//            jsonObject.getString("firstName"),
//            jsonObject.getString("lastName")
//        )
//    }
//

//    private fun createPayload(user: LiveData<UserResponse>): Payload {
//        val gson = Gson()
//        val data = gson.toJson(user).toByteArray()
//        return Payload.fromBytes(data)
//    }
//

//
//    }
}

data class Endpoint(
    val id: String,
    val name: String
)