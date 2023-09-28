package com.example.gst

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Garage.newInstance] factory method to
 * create an instance of this fragment.
 */
class Garage : Fragment() {

    private lateinit var adapter: GarageAdapter
    private lateinit var garageRecyclerView: RecyclerView
    private lateinit var garageArrayList: ArrayList<GarageData>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userLocation: GeoPoint

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize user's location (replace with actual coordinates)
        userLocation = GeoPoint(9.020478527484224, 38.759949051401776)

        dataInitialize(userLocation)

        val layoutManager = LinearLayoutManager(context)
        garageRecyclerView = view.findViewById(R.id.rvGarage)
        garageRecyclerView.layoutManager = layoutManager
        garageRecyclerView.setHasFixedSize(true)
        adapter = GarageAdapter(garageArrayList)
        garageRecyclerView.adapter = adapter


//        adapter.onItemClickListener = { gasStation ->
//            val intent = Intent(requireContext(), GasStationDetailsExpand::class.java)
//
//            // Retrieve the document ID associated with the gas station data
//            val gasStationId = gasStationsWithIds.find { it.second == gasStation }?.first
//
//            intent.putExtra("gasStationId", gasStationId) // Pass the document ID
//            intent.putExtra("gasStationName", gasStation.gasStationName)
//            intent.putExtra("gasStationAddress", gasStation.gasStationAddress)
//            intent.putExtra("gasStationPhone", gasStation.gasStationPhone)
//            gasStation.location?.let { intent.putExtra("gasStationLatitude", it.latitude) }
//            gasStation.location?.let { intent.putExtra("gasStationLongitude", it.longitude) }
//            intent.putExtra("gasStationQueueLength", gasStation.gasStationQueueLength)
//            startActivity(intent)
//        }
    }

    // Store the gas stations with their document IDs
    private lateinit var garagesWithIds: MutableList<Pair<String, GarageData>>

    private fun dataInitialize(userLocation: GeoPoint) {
        garageArrayList = arrayListOf<GarageData>()

        // Reference to the "gas_stations" collection in Firestore
        val collectionReference = firestore.collection("garages")

        // Fetch data from Firestore without sorting
        collectionReference
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                garagesWithIds = mutableListOf()

                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val garage = documentSnapshot.toObject(GarageData::class.java)

                    if (garage != null) {
                        val documentId = documentSnapshot.id
                        garagesWithIds.add(Pair(documentId, garage))
                        garageArrayList.add(garage)
                    }
                }

                // Sort the gasArrayList by distance
                garageArrayList.sortBy { it.location?.let { it1 -> calculateHaversineDistance(it1, userLocation) } }
                adapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
                Log.e("Firestore", "Error fetching data: ${exception.message}")
            }
    }

    // Using haversine formula to calculate the distance between user's location and gas station location
    private fun calculateHaversineDistance(
        location1: GeoPoint,
        location2: GeoPoint
    ): Double {
        val radiusOfEarth = 6371.0 // Earth's radius in kilometers

        // Convert latitude and longitude from degrees to radians
        val lat1Rad = Math.toRadians(location1.latitude)
        val lon1Rad = Math.toRadians(location1.longitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val lon2Rad = Math.toRadians(location2.longitude)

        // Haversine formula
        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad
        val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radiusOfEarth * c
    }
}