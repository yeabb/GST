package com.example.gst

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GarageDetailsExpand : AppCompatActivity() {

    private lateinit var btGarageToGoogleMap: Button
    private lateinit var tvGarageTitle: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garage_details_expand)


        btGarageToGoogleMap = findViewById(R.id.btGarageToGoogleMap)
        tvGarageTitle = findViewById(R.id.tvGarageTitle)

        val garageId = intent.getStringExtra("garageId").toString()
        val garageName = intent.getStringExtra("garageName")
        val garagePhone = intent.getStringExtra("garagePhone")
        val garageLatitude = intent.getDoubleExtra("garageLatitude", 0.0)
        val garageLongitude = intent.getDoubleExtra("garageLongitude", 0.0)


        tvGarageTitle.text = garageName


        btGarageToGoogleMap.setOnClickListener {
            Log.d("GooglenMap Button Click", "Google map Button clicked!")
            openGoogleMapsDirections(garageLatitude, garageLongitude)
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