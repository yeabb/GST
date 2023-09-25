package com.example.gst

import android.os.Bundle
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
 * Use the [Signup.newInstance] factory method to
 * create an instance of this fragment.
 */
class Signup : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btSignup: Button
    private lateinit var etSignupEmail: EditText
    private lateinit var etSignupPassword: EditText
    private lateinit var tvSignupToLogin : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)


        firebaseAuth = FirebaseAuth.getInstance()
        btSignup = view.findViewById(R.id.btSignup)
        etSignupEmail = view.findViewById(R.id.etSignupEmail)
        etSignupPassword = view.findViewById(R.id.etSignupPassword)
        tvSignupToLogin = view.findViewById(R.id.tvSignupToLogin)

        btSignup.setOnClickListener{
            val email = etSignupEmail.text.toString()
            val password = etSignupPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navigateToLoginFragment()

                        } else {
                            Toast.makeText(requireContext(), "Something went wrong please try again", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else {
                Toast.makeText(requireContext(), "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        tvSignupToLogin.setOnClickListener {
            navigateToLoginFragment()
        }


        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Signup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Signup().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }

    private fun navigateToLoginFragment() {
        // Create an instance of the login fragment
        val loginFragment = Login()

        // Get the FragmentManager
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        // Start a new FragmentTransaction
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the current fragment with the login fragment
        transaction.replace(R.id.flFragment, loginFragment)

        // Add the transaction to the back stack (optional)
        // transaction.addToBackStack(null)

        transaction.commit()
    }
}