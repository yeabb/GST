package com.example.gst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class CarAdapter(private var carsArr: ArrayList<CarsSellData>):
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    var onItemClickListener: ((CarsSellData) -> Unit)? = null

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sicarImageLeftWing: ShapeableImageView = itemView.findViewById(R.id.sicarImageLeftWing)
        val tvCarMake: TextView = itemView.findViewById(R.id.tvCarMake)
        val tvCarModelName: TextView = itemView.findViewById(R.id.tvCarModelName)
        val tvCarModelYear: TextView = itemView.findViewById(R.id.tvCarModelYear)
        val tvCarStatus: TextView = itemView.findViewById(R.id.tvCarStatus)

        fun bind(car: CarsSellData) {
            sicarImageLeftWing.setImageResource(R.drawable.ic_cars)
            tvCarMake.text = car.carMake

            // Set the car status text based on the data
            tvCarStatus.text = car.carStatus.toString()

            itemView.setOnClickListener {
                onItemClickListener?.invoke(car)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_car, parent, false)
        return CarViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return carsArr.size
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val currentItem = carsArr[position]
        holder.sicarImageLeftWing.setImageResource(R.drawable.ic_cars)
        holder.tvCarModelName.text = currentItem.carModelName

        // Set the queue status text based on the data
        holder.tvCarStatus.text = currentItem.carStatus.toString()

        holder.bind(currentItem)
    }
}
