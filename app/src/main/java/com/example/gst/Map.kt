package com.example.gst

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Map.newInstance] factory method to
 * create an instance of this fragment.
 */
class Map : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val db = FirebaseFirestore.getInstance()

    //Set up a custom map style so that we can remove all the markers on the map
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





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Map.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Map().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.setMapStyle(MapStyleOptions(customMapStyle))

        val userLocation = LatLng(9.020478527484224, 38.759949051401776)
        val gasIcon = createGasStationIcon()
        val userIcon = creatUserIcon()


        // Query the Firestore collection and add markers for each gas station
        db.collection("gas_stations")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val gasStation = document.data
                    val gasStationName = gasStation["gasStationName"] as String
                    val latitude = (gasStation["location"] as com.google.firebase.firestore.GeoPoint).latitude
                    val longitude = (gasStation["location"] as com.google.firebase.firestore.GeoPoint).longitude

                    val latLng = LatLng(latitude, longitude)

                    googleMap?.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(gasStationName)
                            .icon(gasIcon)
                    )
                }

                // Optionally, move the camera to show all markers
                val firstMarker = documents.firstOrNull()
                if (firstMarker != null) {
                    val firstLatLng = LatLng(
                        (firstMarker["location"] as com.google.firebase.firestore.GeoPoint).latitude,
                        (firstMarker["location"] as com.google.firebase.firestore.GeoPoint).longitude
                    )



                    googleMap?.addMarker(
                        MarkerOptions()
                            .position(userLocation)
                            .title("Your Location")
                            .icon(userIcon)
                    )

                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13.0f))
                }
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

    //Create user pin icon
    private fun creatUserIcon(): BitmapDescriptor {
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
