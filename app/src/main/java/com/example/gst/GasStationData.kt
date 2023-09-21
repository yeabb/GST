package com.example.gst

import com.google.firebase.firestore.GeoPoint

data class GasStationData(
    var gasStationName: String = "",
    var gasStationAddress: String = "",
    var gasStationPhone: String = "",
    var location : GeoPoint? = null,
    var gasStationQueueLength: Int = 0
)
