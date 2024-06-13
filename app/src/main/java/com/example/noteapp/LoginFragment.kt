package com.example.noteapp

import android.content.Context
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
import com.example.noteapp.databinding.FragmentLoginBinding
import com.example.noteapp.others.Cons
import com.example.noteapp.room.entities.Note
import com.example.noteapp.room.entities.User
import com.example.noteapp.viewmodel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel
    private val list = ArrayList<User>()
    private lateinit var reference : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        reference = FirebaseDatabase.getInstance().getReference("notes")
        isFirebaseAuth()
        onObserve()
        onClick()

        return binding.root
    }

    private fun isFirebaseAuth() {
        val list = ArrayList<User>()
        viewModel.getUser().observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
        }
        if (list.isNullOrEmpty()){
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<Note>()

                    for (item in snapshot.children){
                        val note = item.getValue(Note::class.java)
                        if (note != null) {
                            noteList.add(note)
                        }
                    }

                    for (item in noteList){

                        val note = Note(
                            id = 0,
                            title = item.title,
                            note = item.note,
                            date = item.date,
                            userName = item.userName,
                            favourite = item.favourite
                        )
                        viewModel.insertNote(note)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Firebase data", "Failed to read value.", error.toException())
                }
            })

            val reference2 : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
            reference2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userList = ArrayList<User>()

                    for (item in snapshot.children){

                        val user = item.getValue(User::class.java)
                        if (user != null){
                            userList.add(user)
                        }
                    }

                    for (item in userList){
                        Log.d("userList","${item}")
                        val user = User(
                            id = 0,
                            firstName = item.firstName,
                            lastName = item.lastName,
                            email = item.email,
                            password = item.password
                        )
                        viewModel.insertUser(user)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Firebase data", "Failed to read value.", error.toException())
                }
            })
        }

    }

    private fun onObserve() {
        viewModel.getUser().observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
        }
    }

    private fun onClick() {
        binding.loginbutton.setOnClickListener {
            if (isValidationCheck()){
                sharedPref("Login","success")
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
        binding.logemail.setOnFocusChangeListener{ _, focused ->
            val email = binding.logemail.text.toString()
            if (!focused){
                if (email.length < 2 ){
                    binding.emailContainer.helperText = "Minimum 2 characters"
                    binding.emailContainer.boxStrokeColor = Color.RED
                }else{
                    binding.emailContainer.helperText = null
                    binding.emailContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }

        binding.logpass.setOnFocusChangeListener{ _, focused ->
            val pass = binding.logpass.text.toString()
            if (!focused){
                if (pass.length < 2 ){
                    binding.passContainer.helperText = "Minimum 2 characters"
                    binding.passContainer.boxStrokeColor = Color.RED
                }else{
                    binding.passContainer.helperText = null
                    binding.passContainer.boxStrokeColor = Color.parseColor("#4899EA")
                }
            }
        }

        binding.logsign.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }


    private fun sharedPref(key : String, value : String) {
        val sharedPreferences = requireContext().getSharedPreferences("shared", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    private fun isValidationCheck(): Boolean {
        val email = binding.logemail.text.toString()
        val pass = binding.logpass.text.toString()

        if ((email != null) && (pass != null)) {
            if ((email.length >= 2) && (pass.length >= 2)){
                val data = list.find { it.email == email && it.password == pass }

                if (data != null){
                    Cons.userName = data.firstName
                    data.firstName?.let { sharedPref("username", it) }
                    return true
                }else{
                    Toast.makeText(requireContext(),"data not matched",Toast.LENGTH_LONG).show()
                    return false
                }
            }
            Toast.makeText(requireContext(),"data < 2",Toast.LENGTH_LONG).show()
            return false
        }
        Toast.makeText(requireContext(),"data is null",Toast.LENGTH_LONG).show()
        return false
    }

}