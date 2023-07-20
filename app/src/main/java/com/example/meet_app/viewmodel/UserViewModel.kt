package com.example.meet_app.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meet_app.api.user.UserRepository
import com.example.meet_app.api.user.UserResponse
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val context: Context,
    application: Application

) : ViewModel() {
    private val _currentUser = MutableLiveData<UserResponse>()
    val currentUser: LiveData<UserResponse> = _currentUser

    private val _users = MutableLiveData<List<UserResponse>>()
    val users: LiveData<List<UserResponse>>
        get() = _users

    //        get() = _currentUser
    private val _discoveredUsers = MutableLiveData<List<Endpoint>>()
    val discoveredUsers: LiveData<List<Endpoint>> = _discoveredUsers

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

    private fun parseUserProfile(userBytes: ByteArray): UserResponse {
        val inputStream = ByteArrayInputStream(userBytes)
        val objectInputStream = ObjectInputStream(inputStream)
        val user = objectInputStream.readObject() as UserResponse
        objectInputStream.close()
        inputStream.close()
        return user
    }

    private val nearByShareClient = Nearby.getConnectionsClient(application)

//    fun startDiscovery() {
//        val options = DiscoveryOptions.Builder()
//            .setStrategy(Strategy.P2P_CLUSTER)
//            .build()
//
//        nearByShareClient.startDiscovery(
//            "User Data",
//            object : EndpointDiscoveryCallback() {
//                override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
//
//                    nearByShareClient.requestConnection(android.os.Build.MODEL, endpointId,   ConnectionLifecycleCallback()
//
//                    )
//
//                }
//
//                @RequiresApi(Build.VERSION_CODES.N)
//                override fun onEndpointLost(endpointId: String) {
//
//                }
//
//            },
//            options
//        )
//            .addOnSuccessListener {
//                Log.d(TAG, "Discovery started")
//            }
//            .addOnFailureListener { exception ->
//                // Handle the exception and show an error message
//                Log.e(TAG, "Discovery failed: ${exception.message}")
//            }
//    }

//    fun startAdvertising() {
//        val advertisingOptions = AdvertisingOptions.Builder()
//            .setStrategy(Strategy.P2P_CLUSTER)
//            .build()
//
//        nearByShareClient.startAdvertising(
//            "User Data", // Service name
//            "com.example.userdata", // Service ID
//            object : ConnectionLifecycleCallback() {
//                override fun onConnectionInitiated(
//                    endpointId: String,
//                    connectionInfo: ConnectionInfo
//                ) {
//                    // Automatically accept the connection on both ends.
//
//                    nearByShareClient.acceptConnection(endpointId, object : PayloadCallback() {
//                        override fun onPayloadReceived(endpointId: String, payload: Payload) {
//                            if (payload.type == Payload.Type.BYTES) {
//                                val userProfileBytes = payload.asBytes()
//                                val userProfile = userProfileBytes?.let { Payload.fromBytes(it) }
//
//                                if (userProfile != null) {
//                                    nearByShareClient.sendPayload(endpointId, userProfile)
//                                }
//
//                            }
//                        }
//
//                        override fun onPayloadTransferUpdate(
//                            p0: String,
//                            p1: PayloadTransferUpdate
//                        ) {
//                            TODO("Not yet implemented")
//                        }
//
//                    })
//
//                }
//
//                override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
//                    if (result.status.isSuccess) {
//                        Log.d(TAG, "Connection successful")
//                    } else {
//                        Log.e(TAG, "Connection failed")
//                    }
//                }
//
//                override fun onDisconnected(endpointId: String) {
//                    Log.d(TAG, "Disconnected from endpoint $endpointId")
//                }
//            },
//            advertisingOptions
//        ).addOnSuccessListener {
//            Log.d(TAG, "Advertising started")
//        }.addOnFailureListener { exception ->
//            // Handle the exception and show an error message
//            Log.e(TAG, "Advertising failed: ${exception.message}")
//        }
//    }

    fun startNearbyConnection( isAdvertising: Boolean){
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()

        val callback = object : ConnectionLifecycleCallback(){
            override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                if (isAdvertising){

                    nearByShareClient.acceptConnection(endpointId, payloadCallback)
                } else {

                    nearByShareClient.requestConnection(
                        android.os.Build.MODEL,
                        endpointId,
                        this
                    )
                }
            }

            override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
                TODO("Not yet implemented")
            }

            override fun onDisconnected(p0: String) {
                TODO("Not yet implemented")
            }

        }

        val  payloadCallback = object : PayloadCallback() {
            override fun onPayloadReceived(p0: String, p1: Payload) {
                TODO("Not yet implemented")
            }

            override fun onPayloadTransferUpdate(p0: String, p1: PayloadTransferUpdate) {
                TODO("Not yet implemented")
            }

        }

    }
    fun stopDiscovery() {
        nearByShareClient.stopDiscovery()
        Log.d(TAG, "Discovery stopped")
    }

    fun stopAdvertising(){
        nearByShareClient.stopAdvertising()
        Log.d(TAG, "Advertising stopped")
    }
    fun shareUser(endpointId: String) {
        val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
        nearByShareClient.sendPayload(endpointId, payload)

    }


    fun getEndpointUser(endpoint: Endpoint): UserResponse? {
        val users = users.value ?: emptyList()

        for (user in users) {
            if (user.deviceName == endpoint.name || user.deviceId == endpoint.id) {
                return user
            }
        }
        return null
    }

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
            Log.d(TAG, "Received payload from $endpointId: $payload")
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            if (update.status == PayloadTransferUpdate.Status.SUCCESS) {
                Log.d(TAG, "Payload sent to $endpointId successfully")
            }
        }
    }




    fun getDiscoverUsers() {
//        getEndpointUser().
    }

    private fun serializeUser(user: UserResponse): ByteArray {
        val data = JSONObject()
        data.put("id", user.id)
        data.put("username", user.username)
        data.put("firstName", user.firstName)
        data.put("lastName", user.lastName)
        return data.toString().toByteArray(Charsets.UTF_8)
    }

    private fun deserializeUser(bytes: ByteArray): UserResponse {
        val jsonString = String(bytes, Charsets.UTF_8)
        val jsonObject = JSONObject(jsonString)
        return UserResponse(
            id = jsonObject.getString("id"),
            username = jsonObject.getString("username"),
            firstName = jsonObject.getString("firstName"),
            lastName = jsonObject.getString("lastName"),
        )
    }
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
//    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
//        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
//            Log.d(TAG, "Endpoint found")
//            val payload = createPayload(currentUser)
//            Nearby.getConnectionsClient(context)
//                .rejectConnection(name, endpointId)
//        }
//
//        override fun onEndpointLost(p0: String) {
//            TODO("Not yet implemented")
//        }
//
//    }
}

data class Endpoint(
    val id: String,
    val name: String
)