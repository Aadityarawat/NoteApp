package com.example.noteapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.databinding.FragmentSplashBinding
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
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }
    private lateinit var maineViewModel: MainViewModel
    private lateinit var reference : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reference = FirebaseDatabase.getInstance().getReference("notes")
        maineViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            //isFirebaseAuth()
            val sharedPreferences = activity?.getSharedPreferences("shared", AppCompatActivity.MODE_PRIVATE)
            var data = sharedPreferences?.getString("username",null)
            Cons.userName = data
            data = sharedPreferences?.getString("Login",null)
            if (data == "success"){
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }
        }, 3000)

        return binding.root
    }

    private fun isFirebaseAuth() {
        val list = ArrayList<User>()
        maineViewModel.getUser().observe(viewLifecycleOwner) {
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
                        Log.d("noteList","${item.note}")
                        val note = Note(
                            id = 0,
                            title = item.title,
                            note = item.note,
                            date = item.date,
                            userName = item.userName,
                            favourite = item.favourite

                        )
                        maineViewModel.insertNote(note)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Firebase data", "Failed to read value.", error.toException())
                }
            })
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val userList = ArrayList<User>()

                    for (item in snapshot.children){
                        val user = item.getValue(User::class.java)
                        if (user != null){
                            userList.add(user)
                        }
                    }

                    for (item in userList){

                        val user = User(
                            id = 0,
                            firstName = item.firstName,
                            lastName = item.lastName,
                            email = item.email,
                            password = item.password
                        )
                        maineViewModel.insertUser(user)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Firebase data", "Failed to read value.", error.toException())
                }
            })
        }

    }

}