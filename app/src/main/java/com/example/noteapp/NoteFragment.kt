package com.example.noteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.others.Cons
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.room.entities.Note
import com.example.noteapp.viewmodel.MainViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private val binding by lazy { FragmentNoteBinding.inflate(layoutInflater) }
    lateinit var mainViewModel : MainViewModel
    var isUpdate = false
    lateinit var newNote : Note
    lateinit var oldNote: Note
    lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setup()
        reference = FirebaseDatabase.getInstance().getReference("notes")
        return binding.root
    }

    private fun setup() {
        try {
            //oldNote = mainViewModel.noteData
            oldNote = Cons.note!!
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true

        }catch (e : Exception){
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val note = binding.etNote.text.toString()

            if (!title.isNullOrEmpty() || !note.isNullOrEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if (isUpdate){
                    newNote = Note(
                        oldNote.id,
                        title,
                        note,
                        formatter.format(Date()),
                        Cons.userName,
                        oldNote.favourite
                    )
                    isUpdate = false
                    Cons.note = null
                    mainViewModel.updateNote(newNote)
                    val re = Regex("[^A-Za-z ]")
                    val child = newNote.title?.let { it1 -> re.replace(it1,"") }
                    if (child != null) {
                        reference.child(child).setValue(newNote)
                    }
                }
                else{
                    newNote = Note(
                        0,
                        title,
                        note,
                        formatter.format(Date()),
                        Cons.userName,
                        false
                    )
                    mainViewModel.insertNote(newNote)
                    val re = Regex("[^A-Za-z ]")
                    val child = newNote.title?.let { it1 -> re.replace(it1,"") }
                    if (child != null) {
                        reference.child(child).setValue(newNote)
                    }
                }

                findNavController().navigate(R.id.action_noteFragment_to_mainFragment)

            }
            else{
                (Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_LONG)).show()
                return@setOnClickListener
            }
        }

        binding.imgback.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_mainFragment)
        }
    }

}