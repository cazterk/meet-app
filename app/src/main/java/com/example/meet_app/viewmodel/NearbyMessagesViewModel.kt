package com.example.meet_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.Strategy

class NearbyConnectionsViewModel(application: Application) : AndroidViewModel(application) {

    // Nearby Connections API entry point
    private val connectionsClient = Nearby.getConnectionsClient(application)

    // Start advertising a service
    fun startAdvertising(name: String, serviceId: String, callback: ConnectionLifecycleCallback) {
        val options = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .build()
        connectionsClient.startAdvertising(name, serviceId, callback, options)
    }

    // Start discovering a service
    fun startDiscovering(serviceId: String, callback: EndpointDiscoveryCallback) {
        val options = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .build()
        connectionsClient.startDiscovery(serviceId, callback, options)
    }

    // Send a payload to an endpoint
    fun sendPayload(endpointId: String, payload: Payload) {
        connectionsClient.sendPayload(endpointId, payload)
    }

    // Disconnect from an endpoint
    fun disconnectFromEndpoint(endpointId: String) {
        connectionsClient.disconnectFromEndpoint(endpointId)
    }
}