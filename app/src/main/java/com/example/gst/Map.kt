package com.example.gst

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class Map : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val db = FirebaseFirestore.getInstance()
    private var showGasStations = true // Initialize to show gas stations by default
    private lateinit var toggleButton: Button

    private val customMapStyle = """
        [
          {
            "featureType": "poi",
            "elementType": "labels",
            "stylers": [
              { "visibility": "off" }
            ]
          },
          {
            "featureType": "transit",
            "elementType": "labels.icon",
            "stylers": [
              { "visibility": "off" }
            ]
          },
          {
            "featureType": "road",
            "elementType": "labels.icon",
            "stylers": [
              { "visibility": "off" }
            ]
          }
        ]
    """.trimIndent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        // Initialize the toggle button and set its click listener
        toggleButton = view.findViewById(R.id.toggleButton)
        toggleButton.setOnClickListener {
            // Toggle between gas stations and garages
            showGasStations = !showGasStations
            updateMapMarkers()
        }

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.setMapStyle(MapStyleOptions(customMapStyle))



        // Initially load markers based on the default choice (gas stations)
        updateMapMarkers()
    }

    private fun updateMapMarkers() {


        // Query the Firestore collection based on the user's choice
        val collectionName = if (showGasStations) "gas_stations" else "garages"
        val fieldName = if (showGasStations) "gasStationName" else "garageName"


        db.collection(collectionName)
            .get()
            .addOnSuccessListener { documents ->

                googleMap?.clear()

                for (document in documents) {
                    val location = document["location"] as? com.google.firebase.firestore.GeoPoint
                    val name = document[fieldName] as? String ?: "Unknown Name"

                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)

                        // Customize the marker icon based on the choice
                        val gasIcon = if (showGasStations) createGasStationIcon() else createGarageIcon()

                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(name)
                                .icon(gasIcon)
                        )
                    }
                }


                // Add a marker for the user's location
                val userLocation = LatLng(9.020478527484224, 38.759949051401776)
                val userIcon = createUserIcon()



                googleMap?.addMarker(
                    MarkerOptions()
                        .position(userLocation)
                        .title("Your Location")
                        .icon(userIcon)
                )

                // Optionally, move the camera to the user's location
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13.0f))


            }
            .addOnFailureListener { exception ->
                // Handle errors here
            }
    }

    // Create gas station icon
    private fun createGasStationIcon(): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gasmarker)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // Create garage icon
    private fun createGarageIcon(): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_garage2)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // Create user pin icon
    private fun createUserIcon(): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.user_marker)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
