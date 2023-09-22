package com.example.gst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView


class GasStationAdapter(private var gasStationsArr: ArrayList<GasStationData>) : RecyclerView.Adapter<GasStationAdapter.GasStationViewHolder>() {

    var onItemClickListener: ((GasStationData) -> Unit)? = null
    inner class GasStationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val siGasStationImage: ShapeableImageView = itemView.findViewById(R.id.siGasStationImage)
        val tvGasStationName: TextView = itemView.findViewById(R.id.tvGasStationName)

        fun bind (gasStation: GasStationData) {
            siGasStationImage.setImageResource(R.drawable.ic_gas)
            tvGasStationName.text = gasStation.gasStationName

            itemView.setOnClickListener {
                onItemClickListener?.invoke(gasStation)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasStationViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_gas, parent, false)
        return GasStationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return gasStationsArr.size
    }

    override fun onBindViewHolder(holder: GasStationViewHolder, position: Int) {
        val currentItem = gasStationsArr[position]
        holder.siGasStationImage.setImageResource(R.drawable.ic_gas)
        holder.tvGasStationName.text = currentItem.gasStationName
        holder.bind(currentItem)
    }

}