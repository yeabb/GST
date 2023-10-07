package com.example.gst

data class CarData(
    var carOwnerFirstName: String = "",
    var carOwnerLastName: String = "",
    var carOwnerPhone: String = "",
    var carPlateNumber: String = "",
    var carMake: String = "",
    var carModelName: String = "",
    var carModelYear: String = "",
    var carStatus : String = "",
    var carImageUrls: ArrayList<String> = ArrayList()


)