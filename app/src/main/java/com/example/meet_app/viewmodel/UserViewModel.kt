package com.example.meet_app.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
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
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
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

    private val nearByShareClient = Nearby.getConnectionsClient(application)

    fun startDiscovery() {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        nearByShareClient.startDiscovery(
            "User Data",
            object : EndpointDiscoveryCallback() {
                override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                    val user = Endpoint(endpointId, info.endpointName)
                    val updatedUsers = discoveredUsers.value.orEmpty() + user
                    _discoveredUsers.value = updatedUsers.toList()

                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onEndpointLost(endpointId: String) {
                    val currentUsers = discoveredUsers.value.orEmpty()
                    val updatedUsers = currentUsers.filter { it.id == endpointId }
                    _discoveredUsers.value = updatedUsers.toList()
                }

            },
            options
        )
            .addOnSuccessListener {
                TODO(" Handle Discovery has started successfully")
            }
            .addOnFailureListener {
                TODO("Handle Discovery has failed ")
            }
    }

    fun stopDiscovery() {
        nearByShareClient.stopDiscovery()
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