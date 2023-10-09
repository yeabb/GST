package com.example.gst

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import java.lang.Math.abs

class CarDetailsExpand : AppCompatActivity() {

    private lateinit var tvCarMake: TextView
    private lateinit var tvCarModelName: TextView
    private lateinit var tvCarModelYear: TextView
    private lateinit var btCallSeller: Button
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var adapter: ImageAdapter
    private lateinit var carImageUrls: ArrayList<String> // To store the image URLs
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details_expand)

        tvCarMake = findViewById(R.id.tvCarMake)
        btCallSeller = findViewById(R.id.btCallSeller)
        tvCarModelName = findViewById(R.id.tvCarModelName)
        tvCarModelYear = findViewById(R.id.tvCarModelYear)
        nextButton = findViewById(R.id.btnNext)
        prevButton = findViewById(R.id.btnPrevious)

        val carId = intent.getStringExtra("carId").toString()
        val carOwnerFirstName = intent.getStringExtra("carOwnerFirstName")
        val carMake = intent.getStringExtra("carMake")
        val carModelName = intent.getStringExtra("carModelName")
        val carModelYear = intent.getStringExtra("carModelYear")
        val carOwnerLastName = intent.getStringExtra("carOwnerLastName")
        val carOwnerPhone = intent.getStringExtra("carOwnerPhone").toString()

        carImageUrls = intent.getStringArrayListExtra("carImageUrls") ?: ArrayList()


        //Update the car details in the view
        tvCarMake.text = carMake
        tvCarModelName.text = carModelName
        tvCarModelYear.text = carModelYear

        init()
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Do not start the auto-scroll runnable here
            }
        })



        //When the user clicks on the callSeller button
        btCallSeller.setOnClickListener {
            val packageManager = packageManager
            val packageName = "com.android.phone"
            val isPhoneAppInstalled = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES) != null

            // If the phone app is not installed, show a toast message to the user
            if (!isPhoneAppInstalled) {
                Toast.makeText(this, "No phone app installed", Toast.LENGTH_SHORT).show()

            } else {
                // Otherwise, start the call intent
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:$carOwnerPhone")
                startActivity(callIntent)
            }
        }



        nextButton.setOnClickListener {
            val currentItem = viewPager2.currentItem
            if (currentItem < adapter.itemCount - 1) {
                viewPager2.setCurrentItem(currentItem + 1, true)
            }
        }

        prevButton.setOnClickListener {
            val currentItem = viewPager2.currentItem
            if (currentItem > 0) {
                viewPager2.setCurrentItem(currentItem - 1, true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
    }



//    private val runnable = Runnable {
//        // This part of the code is removed to prevent auto-scrolling
//    }




    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init() {
        viewPager2 = findViewById(R.id.viewPager2)
        handler = Handler(Looper.myLooper()!!)

        // Create the adapter with the retrieved carImageUrls
        adapter = ImageAdapter(carImageUrls, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }
}
