package com.example.gst

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GasStationDetailsExpand : AppCompatActivity() {

    private lateinit var btToGoogleMap: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station_details_expand)

        btToGoogleMap = findViewById(R.id.btToGoogleMap)
        val destinationLatitude = 40.689247 // Replace with the latitude of your destination
        val destinationLongitude = -74.044502 // Replace with the longitude of your destination

        btToGoogleMap.setOnClickListener {
            Log.d("Button Click", "Button clicked!")
            openGoogleMapsDirections(destinationLatitude, destinationLongitude)
        }

    }

    fun openGoogleMapsDirections(latitude: Double, longitude: Double) {
        val uri = "google.navigation:q=$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.apps.maps") // Specify the package to ensure it opens in Google Maps

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Google Maps app is not installed, handle this case
            // We can open the web version of Google Maps or prompt the user to install the app.
            Toast.makeText(this, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()

        }
    }

}