package com.example.meet_app.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import com.google.gson.Gson
import com.google.gson.JsonParseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val context: Context,
    application: Application,

    ) : ViewModel() {
    private val _currentUser = MutableLiveData<UserEntity>()
    val currentUser: LiveData<UserEntity> = _currentUser

    private val _discoveredUsers = mutableStateListOf<UserEntity>()
    val discoveredUsers: List<UserEntity> get() = _discoveredUsers

    private val nearByShareClient = Nearby.getConnectionsClient(application)


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

    fun updateProfileImage(userId: String, profileImage: String) {
        viewModelScope.launch {
            userRepository.updateProfileImage(userId, profileImage)
        }
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

    private val payloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {

                val userJson = payload.asBytes()?.toString(Charsets.UTF_8)
                userJson?.let { jsonString ->

                    try {
                        val payloadData = JSONObject(jsonString)
                        val userDataJon = payloadData.getJSONObject("user_data")
                        val receivedUser =
                            Gson().fromJson(userDataJon.toString(), UserEntity::class.java)

                        var profilePictureBase64 = payloadData.getString("profile_picture")
                        val profilePictureBytes =
                            Base64.decode(profilePictureBase64, Base64.DEFAULT)

                        val updatedProfilePictureUrl =
                            saveProfilePictureToStorage(receivedUser.id, profilePictureBytes)

                        receivedUser.profileImage = updatedProfilePictureUrl.toString()
                        // Check if the received user already exists in the list based on their ID
                        when (_discoveredUsers.find { it.id == receivedUser.id }) {
                            null -> {
                                _discoveredUsers.add(receivedUser)
                            }

                            else -> {}
                        }


                    } catch (e: JsonParseException) {
                        Log.e(TAG, "Error parsing received JSON response: ${e.message}")
                    }
                }

            } else {
                Log.e(TAG, "Received payload of unexpected type:${payload.type}")
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

            val profilePictureBitmap: Bitmap? = currentUser.profileImage?.let {
                loadUserProfilePicture(
                    it
                )
            }

            val profilePictureBytes = profilePictureBitmap?.let { bitmapToByteArray(it) }

            val payloadData = JSONObject()
            payloadData.put("user_data", JSONObject(currentUserJson))
            payloadData.put(
                "profile_picture",
                Base64.encodeToString(profilePictureBytes, Base64.DEFAULT)
            )

            val payloadJson = payloadData.toString()

            nearByShareClient.sendPayload(
                endpointId,
                Payload.fromBytes(payloadJson.toByteArray())
            )

            Log.d(TAG, "currentUserJson: $payloadJson")
            Log.d(TAG, "profilePicture: ${profilePictureBytes?.size} bytes")
        }
    }


    private fun loadUserProfilePicture(profilePictureFilePath: String): Bitmap? {
        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeFile(profilePictureFilePath, options)
        } catch (e: Exception) {
            e.printStackTrace()
            //
            null
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()

    }

    private fun saveProfilePictureToStorage(
        userId: String,
        profileImageBytes: ByteArray
    ) {
        try {
            val fileName = "profile_profile_$userId.jpg"
            val file = File(context.filesDir, fileName)
            FileOutputStream(file).use { fileOutputStream ->
                fileOutputStream.write(profileImageBytes)
                Log.d(TAG, "Profile pictures saved to: ${file.absolutePath}")

            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving profile picture:${e.message}")
        }
    }
}