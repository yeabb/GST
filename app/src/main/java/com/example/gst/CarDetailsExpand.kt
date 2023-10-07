package com.example.gst

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import java.lang.Math.abs

class CarDetailsExpand : AppCompatActivity() {

    private lateinit var tvCarMake: TextView
    private lateinit var  viewPager2: ViewPager2
    private lateinit var handler : Handler
    private lateinit var carImageUrls:ArrayList<Int>
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_expand)

        tvCarMake = findViewById(R.id.tvCarMake)

        val carId = intent.getStringExtra("carId").toString()
        val carOwnerFirstName = intent.getStringExtra("carOwnerFirstName")
        val carMake = intent.getStringExtra("carMake")
        val carOwnerLastName = intent.getStringExtra("carOwnerLastName")
        val carOwnerPhone = intent.getStringExtra("carOwnerPhone")
//        val carImageUrls = intent.getStringArrayListExtra("carImageUrls")


        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }
        })


        tvCarMake.text = carMake

    }

    override fun onPause() {
        super.onPause()

        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }

    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer(){
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init(){
        viewPager2 = findViewById(R.id.viewPager2)
        handler = Handler(Looper.myLooper()!!)
        carImageUrls = ArrayList()

        carImageUrls.add(R.drawable.ic_car2)
        carImageUrls.add(R.drawable.ic_garage)
        carImageUrls.add(R.drawable.ic_gas)


        adapter = ImageAdapter(carImageUrls, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
}
