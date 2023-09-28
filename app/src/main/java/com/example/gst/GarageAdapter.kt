package com.example.gst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class GarageAdapter(private var garagesArr: ArrayList<GarageData>) :
    RecyclerView.Adapter<GarageAdapter.GarageViewHolder>() {

    var onItemClickListener: ((GarageData) -> Unit)? = null

    inner class GarageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val siGarageImage: ShapeableImageView = itemView.findViewById(R.id.siGarageImage)
        val tvGarageName: TextView = itemView.findViewById(R.id.tvGarageName)


        fun bind(garage: GarageData) {
            siGarageImage.setImageResource(R.drawable.ic_garage2) // Use the appropriate garage image resource
            tvGarageName.text = garage.garageName

            // Set the queue status text based on the data

            itemView.setOnClickListener {
                onItemClickListener?.invoke(garage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_garage, parent, false)
        return GarageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return garagesArr.size
    }

    override fun onBindViewHolder(holder: GarageViewHolder, position: Int) {
        val currentItem = garagesArr[position]
        holder.siGarageImage.setImageResource(R.drawable.ic_garage2) // Use the appropriate garage image resource
        holder.tvGarageName.text = currentItem.garageName

        // Set the queue status text based on the data

        holder.bind(currentItem)
    }
}
