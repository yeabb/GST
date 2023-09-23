package com.example.gst

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.Map

class GasStationDetailsExpand : AppCompatActivity() {

    private lateinit var btToGoogleMap: Button
    private lateinit var tvStationTitle: TextView
    private lateinit var btReport: Button
    private lateinit var etNumberOfCars: EditText
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas_station_details_expand)

        firestore = FirebaseFirestore.getInstance()

        btToGoogleMap = findViewById(R.id.btToGoogleMap)
        tvStationTitle = findViewById(R.id.tvStationTitle)
        btReport = findViewById(R.id.btReport)
        etNumberOfCars = findViewById(R.id.etNumberOfCars)

        val gasStationId = intent.getStringExtra("gasStationId").toString()
        val gasStationName = intent.getStringExtra("gasStationName")
        val gasStationPhone = intent.getStringExtra("gasStationPhone")
        val gasStationLatitude = intent.getDoubleExtra("gasStationLatitude", 0.0)
        val gasStationLongitude = intent.getDoubleExtra("gasStationLongitude", 0.0)
        val gasStationQueueLength = intent.getIntExtra("gasStationQueueLength", 0)

        btToGoogleMap.setOnClickListener {
            Log.d("GooglenMap Button Click", "Google map Button clicked!")
            openGoogleMapsDirections(gasStationLatitude, gasStationLongitude)
        }

        btReport.setOnClickListener {
            Log.d("Report Button click", "Report button clicked!")
            val newQueueValString = etNumberOfCars.text.toString()

            // Check if the input is empty or not
            if (newQueueValString.isNotEmpty()) {
                val newQueueVal = newQueueValString.toInt()
                updateGasStationQueueLength(gasStationId, newQueueVal)
            } else {
                // Handle the case where the input is empty
                Toast.makeText(this, "Please enter a valid number of cars.", Toast.LENGTH_SHORT).show()
            }
        }

        tvStationTitle.text = gasStationName
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

    fun updateGasStationQueueLength(gasStationId: String, newQueueVal: Int) {
        val data = hashMapOf(
            "gasStationQueueLength" to newQueueVal
        )

        // Update the specific field in Firestore
        firestore.collection("gas_stations")
            .document(gasStationId)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                // Handle success
                // The specific field has been successfully updated in Firestore
                Toast.makeText(this, "Queue length updated successfully.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle failure
                // There was an error updating the specific field in Firestore
                Toast.makeText(this, "Failed to update queue length: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
