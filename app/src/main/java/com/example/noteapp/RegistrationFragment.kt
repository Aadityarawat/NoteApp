package com.example.noteapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.databinding.FragmentRegistrationBinding
import com.example.noteapp.room.entities.User
import com.example.noteapp.viewmodel.MainViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val binding by lazy { FragmentRegistrationBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel
    private lateinit var reference : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reference = Firebase.database.reference
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        onClick()
        return binding.root
    }

    private fun onClick() {
        binding.logsign.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment2)
        }
        binding.signBackbtn.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment2)
        }

        binding.signinbutton.setOnClickListener {
            if (isValidationCheck()){
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment2)
            }
        }

        binding.signFirstTV.setOnFocusChangeListener { _, hasFocus ->
            val first = binding.signFirstTV.text.toString()
            if (!hasFocus){
                if (first.length < 2 ){
                    binding.signfirstContainer.helperText = "Minimum 2 characters"
                    binding.signfirstContainer.boxStrokeColor = Color.RED
                }else{
                    binding.signfirstContainer.helperText = null
                    binding.signfirstContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }

        binding.signLastTV.setOnFocusChangeListener { _, hasFocus ->
            val last = binding.signLastTV.text.toString()
            if (!hasFocus){
                if (last.length < 2 ){
                    binding.signLastContainer.helperText = "Minimum 2 characters"
                    binding.signLastContainer.boxStrokeColor = Color.RED
                }else{
                    binding.signLastContainer.helperText = null
                    binding.signLastContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }

        binding.signemailTV.setOnFocusChangeListener { _, hasFocus ->
            val first = binding.signemailTV.text.toString()
            if (!hasFocus){
                if (first.length < 2 ){
                    binding.signemailContainer.helperText = "Minimum 2 characters"
                    binding.signemailContainer.boxStrokeColor = Color.RED
                }else{
                    binding.signemailContainer.helperText = null
                    binding.signemailContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }

        binding.signpassTV.setOnFocusChangeListener { _, hasFocus ->
            val first = binding.signpassTV.text.toString()
            if (!hasFocus){
                if (first.length < 2 ){
                    binding.signpassContainer.helperText = "Minimum 2 characters"
                    binding.signpassContainer.boxStrokeColor = Color.RED
                }else{
                    binding.signpassContainer.helperText = null
                    binding.signpassContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }
    }

    /*private fun isFirebaseAuth(): Boolean {
        val email = binding.signemailTV.text.toString()
        val pass = binding.signpassTV.text.toString()
        var flag = false
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    flag = true
                } else {
                    Toast.makeText(requireContext(), "Unable to create on Firebase", Toast.LENGTH_LONG).show()
                    flag = false
                }
            }
        return flag
    }*/

    private fun isValidationCheck(): Boolean {
        val email = binding.signemailTV.text.toString()
        val pass = binding.signpassTV.text.toString()
        val first = binding.signFirstTV.text.toString()
        val last = binding.signLastTV.text.toString()

        if ((email != null) && (pass != null)) {
            if ((email.length >= 2) && (pass.length >= 2) && (first.length >= 2) && (last.length >= 2)){
                //reference.child("users").push()
                val data =
                    User(
                        email = email,
                        password = pass,
                        firstName = first,
                        lastName = last,
                        id = 0
                    )

                Log.d("ttag","${reference.key}")
                if (data != null) {
                    viewModel.insertUser(data)
                }
                val re = Regex("[^A-Za-z ]")
                val child = data.email?.let { re.replace(it,"") }
                // addOnSuccessListener is optional callback function from the firebase.
                if (child != null) {
                    reference.child("users").child(child).setValue(data).addOnSuccessListener {
                        Toast.makeText(requireContext(),"Data has been added in the Firebase",Toast.LENGTH_LONG).show()
                    }
                }
                //reference.child("users").child("1").setValue(data)
                return true
            }
            return false
        }
        return false
    }

}