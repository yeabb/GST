package com.example.gst

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Gas.newInstance] factory method to
 * create an instance of this fragment.
 */
class Gas : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var adapter: GasStationAdapter
    private lateinit var gasRecyclerView: RecyclerView
    private lateinit var gasArrayList: ArrayList<GasStationData>
    lateinit var imageId : Array<Int>
    lateinit var gasStationDetails : Array<String>

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gas, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Gas.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Gas().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataInitialize()
        val layoutManager = LinearLayoutManager(context)
        gasRecyclerView = view.findViewById(R.id.rvGas)
        gasRecyclerView.layoutManager = layoutManager
        gasRecyclerView.setHasFixedSize(true)
        adapter = GasStationAdapter(gasArrayList)
        gasRecyclerView.adapter = adapter

        adapter.onItemClickListener = { gasStation ->
            val intent = Intent(requireContext(), GasStationDetailsExpand::class.java)
//            intent.putExtra("gasStation", gasStation)
            startActivity(intent)
        }


    }



    private fun dataInitialize(){

        gasArrayList = arrayListOf<GasStationData>()

        imageId = arrayOf(
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
            R.drawable.ic_gas,
        )

        gasStationDetails = arrayOf(
            "Total Kazanchis Akababi Branch",
            "Total Bole Akababi Branch",
            "Shell Yerer Akababi Branch",
            "Total Gerji Akababi Branch",
            "Shell Jackrose Akababi Branch",
            "Oil Libya 4 kilo Akababi Branch",
            "Total 6 kilo Akababi Branch",
            "Shell 5 kilo Akababi Branch",
            "Oil Libya Mexico Akababi Branch",
            "Total Merkato Akababi Branch",

            )

        for (i in imageId.indices){
            val gasStations = GasStationData(imageId[i], gasStationDetails[i])
            gasArrayList.add(gasStations)
        }

    }

}