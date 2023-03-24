package com.example.meet_app.viewmodel

import android.app.Application
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
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    application: Application
) : ViewModel() {
    private val _currentUser = MutableLiveData<UserResponse>()
    val currentUser: LiveData<UserResponse> = _currentUser
//        get() = _currentUser


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
            object : EndpointDiscoveryCallback(){
                override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                  val metaData = info.endpointName.split("|")
                    if (metaData.size == 2 && metaData [0] == "My Data"){
                        nearByShareClient.requestConnection(
                            "User Data",
                            endpointId,
                            object : ConnectionLifecycleCallback(){
                                override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
//                                    nearByShareClient.acceptConnection(endpointId,)
                                }

                                override fun onConnectionResult(
                                    p0: String,
                                    p1: ConnectionResolution
                                ) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDisconnected(p0: String) {
                                    TODO("Not yet implemented")
                                }

                            }
                        )
                    }
                }

                override fun onEndpointLost(endpointId: String) {
                    TODO("Not yet implemented")
                }

            },
            options
        )
    }

    fun sendUSerData(endpointId: String){
        val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
        nearByShareClient.sendPayload(endpointId, payload)

    }
//    fun UserResponse.toPayload(): Payload {
//        val data = JSONObject().apply {
//            put("id", id)
//            put("username", username)
//            put("firstName", firstName)
//            put("lastName", lastName)
//        }.toString()
//        return Payload.fromBytes(data.toByteArray())
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
//    fun shareProfile(context: Context) {
//        val payload = Payload.fromBytes(currentUser.value.toString().toByteArray())
//        Nearby.getConnectionsClient(context)
//            .startAdvertising(
//                "User profile",
//                BuildConfig.APPLICATION_ID,
//                payload,
//                object : ConnectionLifecycleCallback() {
//                    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
//                        Nearby.getConnectionsClient(context)
//                            .acceptConnection(endpointId, object : PayloadCallback() {
//                                override fun onPayloadReceived(
//                                    endpointId: String,
//                                    payload: Payload
//                                ) {
//
//                                    val userData = payload.asBytes()
//                                    if (userData != null) {
//                                        val userDataString = userData.toString(Charsets.UTF_8)
//                                        val user =  Gson().fromJson(userDataString, User::class.java)
//                                    }
//                                }
//
//                                override fun onPayloadTransferUpdate(
//                                    p0: String,
//                                    p1: PayloadTransferUpdate
//                                ) {
//                                    TODO("Not yet implemented")
//                                }
//
//                            })
//                    }
//
//                    override fun onConnectionResult(p0: String, p1: ConnectionResolution) {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onDisconnected(p0: String) {
//                        TODO("Not yet implemented")
//                    }
//
//                }
//            )
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