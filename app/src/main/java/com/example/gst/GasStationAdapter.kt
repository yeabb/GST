package com.example.gst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class GasStationAdapter(private var gasStationsArr: ArrayList<GasStationData>) :
    RecyclerView.Adapter<GasStationAdapter.GasStationViewHolder>() {

    var onItemClickListener: ((GasStationData) -> Unit)? = null

    inner class GasStationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val siGasStationImage: ShapeableImageView = itemView.findViewById(R.id.siGasStationImage)
        val tvGasStationName: TextView = itemView.findViewById(R.id.tvGasStationName)
        val tvGasStationQueueLength: TextView = itemView.findViewById(R.id.tvGasStationQueueLength)

        fun bind(gasStation: GasStationData) {
            siGasStationImage.setImageResource(R.drawable.ic_gas2)
            tvGasStationName.text = gasStation.gasStationName

            // Set the queue status text based on the data
            val queueStatusText = gasStation.gasStationQueueLength
            tvGasStationQueueLength.text = queueStatusText.toString()

            itemView.setOnClickListener {
                onItemClickListener?.invoke(gasStation)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasStationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_gas, parent, false)
        return GasStationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gasStationsArr.size
    }

    override fun onBindViewHolder(holder: GasStationViewHolder, position: Int) {
        val currentItem = gasStationsArr[position]
        holder.siGasStationImage.setImageResource(R.drawable.ic_gas2)
        holder.tvGasStationName.text = currentItem.gasStationName

        // Set the queue status text based on the data
        val queueStatusText = currentItem.gasStationQueueLength
        holder.tvGasStationQueueLength.text = queueStatusText.toString()

        holder.bind(currentItem)
    }
}
