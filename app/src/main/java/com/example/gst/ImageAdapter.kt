package com.example.gst

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.squareup.picasso.Picasso

class ImageAdapter(private val carImageUrls: ArrayList<String>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.car_image_container, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Load the image from the URL using Picasso
        Picasso.get()
            .load(carImageUrls[position])
            .placeholder(R.drawable.ic_car2) // Optional: Placeholder image
            .error(R.drawable.ic_car) // Optional: Error image
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return carImageUrls.size
    }

//    private val runnable = Runnable {
//        carImageUrls.addAll(carImageUrls)
//        notifyDataSetChanged()
//    }
}
