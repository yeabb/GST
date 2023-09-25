package com.example.gst

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gst.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase (Add this line)
        FirebaseApp.initializeApp(this)

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val currentUser = firebaseAuth.currentUser
        Log.d("MainActivity", "Current User: $currentUser")

        if (currentUser == null) {
            hideBottomNavigation()
            // User is not authenticated, redirect to the login screen (Login fragment)
            replaceFragment(Signup())
        } else {
            // User is authenticated, enable the bottom navigation menu
            showBottomNavigation()
            replaceFragment(Home())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> replaceFragment(Home())
                R.id.miRefer -> replaceFragment(Refer())
                R.id.miGas -> replaceFragment(Gas())
                R.id.miPay -> replaceFragment(Pay())
                R.id.miMap -> replaceFragment(Map())
            }
            true
        }
    }

    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, fragment)
        fragmentTransaction.commit()
    }
}
