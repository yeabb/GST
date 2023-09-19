package com.example.gst

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class Gas : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapter: GasStationAdapter
    private lateinit var gasRecyclerView: RecyclerView
    private lateinit var gasArrayList: ArrayList<GasStationData>
    private lateinit var databaseReference: DatabaseReference

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
        return inflater.inflate(R.layout.fragment_gas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference.child("gas_stations")

        dataInitialize()

        val layoutManager = LinearLayoutManager(context)
        gasRecyclerView = view.findViewById(R.id.rvGas)
        gasRecyclerView.layoutManager = layoutManager
        gasRecyclerView.setHasFixedSize(true)
        adapter = GasStationAdapter(gasArrayList)
        gasRecyclerView.adapter = adapter

        adapter.onItemClickListener = { gasStation ->
            val intent = Intent(requireContext(), GasStationDetailsExpand::class.java)
            // You can pass the selected gas station data to the details activity here
            // intent.putExtra("gasStation", gasStation)
            startActivity(intent)
        }
    }

    private fun dataInitialize() {
        gasArrayList = arrayListOf<GasStationData>()

        // Attach a ValueEventListener to fetch data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gasArrayList.clear() // Clear the existing list
                for (dataSnapshot in snapshot.children) {
                    val gasStation = dataSnapshot.getValue(GasStationData::class.java)
                    gasStation?.let { gasArrayList.add(it) }
                }
                adapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors here
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }
}
