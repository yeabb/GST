package com.example.gst


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gst.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "onCreate")
        replaceFragment(Login())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.miHome -> replaceFragment(Home())
                R.id.miRefer -> replaceFragment(Refer())
                R.id.miGas -> replaceFragment(Gas())
                R.id.miPay -> replaceFragment(Pay())
                R.id.miMap -> replaceFragment(Map())

            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, fragment)
        fragmentTransaction.commit()

    }
}