package com.example.gst

data class GasStationData(
    var gasStationName: String = "",
    var gasStationAddress: String = "",
    var gasStationPhone: String = "",
    var gasStationLatitude: Double = 0.0,
    var gasStationLongitude: Double = 0.0,
    var gasStationQueueLength: Int = 0
)
