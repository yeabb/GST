package com.example.gst

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btLogin: Button
    private lateinit var etLoginEmail: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var tvLoginToSignUp: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LoginFragment", "Login fragment created")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        btLogin = view.findViewById(R.id.btLogin)
        etLoginEmail = view.findViewById(R.id.etLoginEmail)
        etLoginPassword = view.findViewById(R.id.etLoginPassword)
        tvLoginToSignUp = view.findViewById(R.id.tvLoginToSignUp)


        btLogin.setOnClickListener{
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            showToolbarAndNavigationView()
                            showBottomNavigation()
                            replaceFragment(Map())
                        }else{
                            val errorMessage = task.exception?.message
                            Toast.makeText(requireContext(), "Wrong email or password, please try again", Toast.LENGTH_SHORT).show()
                        }

                    }
            }else{
                Toast.makeText(requireContext(), "Email and password can not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        tvLoginToSignUp.setOnClickListener {
            navigateToSignupFragment()
        }

        return view
    }

    private fun showToolbarAndNavigationView() {
        // Assuming that MainActivity has a function to show the bottom navigation menu
        if (activity is MainActivity) {
            (activity as MainActivity).showToolbarAndNavigationView()
        }
    }

    private fun showBottomNavigation() {
        // Assuming that MainActivity has a function to show the bottom navigation menu
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNavigation()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        // Assuming that MainActivity has a function to replace fragments
        if (activity is MainActivity) {
            (activity as MainActivity).replaceFragment(fragment)
        }
    }



    private fun navigateToSignupFragment() {
        // Create an instance of the login fragment
        val signupFragment = Signup()

        // Get the FragmentManager
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // Start a new FragmentTransaction
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the login fragment
        transaction.replace(R.id.flFragment, signupFragment)

        // Add the transaction to the back stack (optional)
        // transaction.addToBackStack(null)

        transaction.commit()
    }
}