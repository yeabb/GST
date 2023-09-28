package com.example.gst

import com.google.firebase.firestore.GeoPoint

data class GarageData(
    var garageName: String = "",
    var garageAddress: String = "",
    var garagePhone: String = "",
    var location : GeoPoint? = null,
)
