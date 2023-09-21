package com.example.gst

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class GasStationDetailsExpand : AppCompatActivity() {

    private lateinit var btToGoogleMap: Button
    private lateinit var tvStationTitle: TextView
    private lateinit var tvStationAddress: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station_details_expand)


        val gasStationName = intent.getStringExtra("gasStationName")
        val gasStationAddress = intent.getStringExtra("gasStationAddress")
        val gasStationPhone = intent.getStringExtra("gasStationPhone")
        val gasStationLatitude = intent.getDoubleExtra("gasStationLatitude", 0.0)
        val gasStationLongitude = intent.getDoubleExtra("gasStationLongitude", 0.0)
        val gasStationQueueLength = intent.getIntExtra("gasStationQueueLength", 0)


        btToGoogleMap = findViewById(R.id.btToGoogleMap)
        tvStationTitle = findViewById(R.id.tvStationTitle)
        tvStationAddress = findViewById(R.id.tvStationAddress)


        tvStationTitle.text = gasStationName
        tvStationAddress.text = gasStationAddress



        btToGoogleMap.setOnClickListener {
            Log.d("Button Click", "Button clicked!")
            openGoogleMapsDirections(gasStationLatitude, gasStationLongitude)
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