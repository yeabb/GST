package com.example.gst

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CarBuyDetailsExpand : AppCompatActivity() {

    private lateinit var tvCarMake: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_buy_details_expand)


        tvCarMake = findViewById(R.id.tvCarMake)

        val carId = intent.getStringExtra("carId").toString()
        val carSellerFirstName = intent.getStringExtra("carSellerFirstName")
        val carMake = intent.getStringExtra("carMake")
        val carSellerLastName = intent.getStringExtra("carSellerLastName")
        val carSellerPhone = intent.getStringExtra("carSellerPhone")



        tvCarMake.text = carMake



    }

}