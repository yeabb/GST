package com.example.gst

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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
    private lateinit var toggleButton: Switch

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

        // Initialize the toggle button
        toggleButton = view.findViewById(R.id.toggleButton)
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Display the list of "Cars for Rent"
                dataInitialize(true)
            } else {
                // Display the list of "Cars for Sale"
                dataInitialize(false)
            }
        }

        val layoutManager = LinearLayoutManager(context)
        carRecyclerView = view.findViewById(R.id.rvCar)
        carRecyclerView.layoutManager = layoutManager
        carRecyclerView.setHasFixedSize(true)
        carArrayList = ArrayList()
        adapter = CarAdapter(carArrayList)
        carRecyclerView.adapter = adapter

        // Initially, display the list of "Cars for Sale"
        dataInitialize(false)
    }

    // Store the cars with their document IDs
    private lateinit var carsWithIds: MutableList<Pair<String, CarsSellData>>

    private fun dataInitialize(isForRent: Boolean) {
        // Clear the existing list
        carArrayList.clear()

        // Reference to the appropriate collection in Firestore
        val collectionReference = if (isForRent) {
            firestore.collection("carsSell")
        } else {
            firestore.collection("carsRent")
        }

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
