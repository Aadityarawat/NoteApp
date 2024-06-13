package com.example.noteapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapp.adapter.NotesAdapter
import com.example.noteapp.databinding.FragmentFavouritesBinding
import com.example.noteapp.others.Cons
import com.example.noteapp.room.entities.Note
import com.example.noteapp.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(), NotesAdapter.NoteItemClickListener, PopupMenu.OnMenuItemClickListener {

    private val binding by lazy { FragmentFavouritesBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var selectedNote : Note
    private lateinit var reference : DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        reference = FirebaseDatabase.getInstance().getReference("notes")
        setup()
        onObserve()
        return binding.root
    }

    private fun setup() {
        binding.recyclerView1.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        notesAdapter = NotesAdapter(requireContext(), this)
        binding.recyclerView1.adapter = notesAdapter
    }
    private fun onObserve() {
        Cons.userName?.let {
            mainViewModel.getFavNote(it).observe(viewLifecycleOwner) {

                notesAdapter.updateList(it)

                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val noteList = mutableListOf<Note>()

                        for (item in snapshot.children){
                            val note = item.getValue(Note::class.java)
                            if (note != null) {
                                noteList.add(note)
                            }
                        }
                        Log.d("Firebase data", "Users List: $noteList")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("Firebase data", "Failed to read value.", error.toException())
                    }

                })
            }
        }

        binding.imgback1.setOnClickListener {
            val sharedPreferences = activity?.getSharedPreferences("shared", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.putString("Login",null)
            editor?.apply()
            findNavController().navigate(R.id.action_mainFragment_to_splashFragment)
            val bottomNavigationView : BottomNavigationView? = activity?.findViewById(R.id.bottomnavigationbar)
            bottomNavigationView?.visibility = View.GONE
        }

        binding.searchimg.setOnClickListener {
            binding.searchSV.visibility = View.VISIBLE
            binding.searchimg.visibility = View.GONE
        }

        binding.searchSV.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchSV.visibility = View.GONE
                binding.searchimg.visibility = View.VISIBLE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null){
                    notesAdapter.filterList(newText)
                }
                return true
            }

        })

    }

    override fun onItemClicked(note: Note) {
        mainViewModel.noteData = note
        Cons.note = note
        findNavController().navigate(R.id.action_favouritesFragment_to_noteFragment)
    }

    override fun onLongItemClicked(note: Note, linerLayout: LinearLayout) {
        selectedNote = note
        popupMenu(linerLayout)
    }

    override fun onFavouriteFalse(note: Note) {
        note.favourite = true
        mainViewModel.updateNote(note)
        Log.d("note.favourite","$note")
    }

    override fun onFavouriteTrue(note: Note) {
        note.favourite = false
        mainViewModel.updateNote(note)
        Log.d("note.favourite","$note")
    }

    private fun popupMenu(linerLayout: LinearLayout){
        val popup = PopupMenu(requireContext(), linerLayout)
        popup.setOnMenuItemClickListener(this@FavouritesFragment)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note){
            selectedNote.title?.let {

                val re = Regex("[^A-Za-z ]")
                val child = re.replace(it,"")

                reference.child(child).removeValue().addOnSuccessListener {
                    Toast.makeText(requireContext(),"Removed from the firebase", Toast.LENGTH_LONG).show()
                }
            }
            mainViewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

}