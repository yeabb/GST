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



class Car : Fragment() {
    private lateinit var adapter: CarAdapter
    private lateinit var carRecyclerView: RecyclerView
    private lateinit var carArrayList: ArrayList<CarsSellData>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userLocation: GeoPoint

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize user's location (replace with actual coordinates)

        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        carRecyclerView = view.findViewById(R.id.rvCar)
        carRecyclerView.layoutManager = layoutManager
        carRecyclerView.setHasFixedSize(true)
        adapter = CarAdapter(carArrayList)
        carRecyclerView.adapter = adapter


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
    private lateinit var carsWithIds: MutableList<Pair<String, CarsSellData>>

    private fun dataInitialize() {
        carArrayList = arrayListOf<CarsSellData>()

        // Reference to the "gas_stations" collection in Firestore
        val collectionReference = firestore.collection("cars")

        // Fetch data from Firestore without sorting
        collectionReference
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                carsWithIds = mutableListOf()

                querySnapshot?.documents?.forEach { documentSnapshot ->
                    val car = documentSnapshot.toObject(CarsSellData::class.java)

                    if (car != null) {
                        val documentId = documentSnapshot.id
                        carsWithIds.add(Pair(documentId, car))
                        carArrayList.add(car)
                    }
                }
                adapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }
            .addOnFailureListener { exception ->
                // Handle any errors here
                Log.e("Firestore", "Error fetching data: ${exception.message}")
            }
    }


}