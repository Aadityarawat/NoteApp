package com.example.noteapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.databinding.FragmentQuotesBinding
import com.example.noteapp.others.Cons
import com.example.noteapp.retrofit.model.Quotes
import com.example.noteapp.room.entities.Note
import com.example.noteapp.viewmodel.MainViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class QuotesFragment : Fragment() {

    private val binding by lazy { FragmentQuotesBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private val quotesList = ArrayList<Quotes>()
    private lateinit var reference : DatabaseReference
    private val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
    private val favList = ArrayList<Note>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reference = FirebaseDatabase.getInstance().getReference("notes")
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getQuotes()
        onObserve()

        return binding.root
    }

    private fun onSetup() {

        if (quotesList.isNotEmpty()){

            /*for (item in favList){
                if (item.title == quotesList[mainViewModel.indexForQuotes].author){
                    data = item
                }
            }*/
            val author = quotesList[mainViewModel.indexForQuotes].author
            val data = favList.firstOrNull { it.title == author }

            binding.quotesTV.text = quotesList[mainViewModel.indexForQuotes].text
            binding.authorTV.text = quotesList[mainViewModel.indexForQuotes].author
            Log.d("quotes data 3","de")
            if (data != null){
                binding.falseFavIV.visibility = View.GONE
                binding.trueFavIV.visibility = View.VISIBLE
            }
            else Log.d("favlist","error")

        }
        binding.previous.setOnClickListener {
            if (mainViewModel.indexForQuotes-1 == 0){
                binding.previous.visibility = View.GONE
            }
            if (mainViewModel.indexForQuotes > 0){
                binding.next.visibility = View.VISIBLE
                binding.quotesTV.text = quotesList[--mainViewModel.indexForQuotes].text
                binding.authorTV.text = quotesList[mainViewModel.indexForQuotes].author
            }
            val author = quotesList[mainViewModel.indexForQuotes].author
            val data = favList.firstOrNull { it.title == author }
            if (data == null){
                binding.falseFavIV.visibility = View.VISIBLE
                binding.trueFavIV.visibility = View.GONE
            }else{
                binding.falseFavIV.visibility = View.GONE
                binding.trueFavIV.visibility = View.VISIBLE
            }
        }
        binding.next.setOnClickListener {
            if (mainViewModel.indexForQuotes == quotesList.size-2){
                binding.next.visibility = View.GONE
            }
            if (mainViewModel.indexForQuotes < quotesList.size-1){
                binding.previous.visibility = View.VISIBLE
                binding.quotesTV.text = quotesList[++mainViewModel.indexForQuotes].text
                binding.authorTV.text = quotesList[mainViewModel.indexForQuotes].author
            }

            val author = quotesList[mainViewModel.indexForQuotes].author
            val data = favList.firstOrNull { it.title == author }

            if (data == null){
                binding.falseFavIV.visibility = View.VISIBLE
                binding.trueFavIV.visibility = View.GONE
            }else{
                binding.falseFavIV.visibility = View.GONE
                binding.trueFavIV.visibility = View.VISIBLE
            }
        }

        binding.shareFL.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_TEXT, quotesList[mainViewModel.indexForQuotes].text)
            startActivity(intent)
        }

        binding.falseFavIV.setOnClickListener {
            binding.trueFavIV.visibility = View.VISIBLE
            binding.falseFavIV.visibility = View.GONE

            val note = Note(
                id = 0,
                title = quotesList[mainViewModel.indexForQuotes].author,
                note = quotesList[mainViewModel.indexForQuotes].text,
                date = formatter.format(Date()),
                userName = Cons.userName,
                favourite = true
            )
            if (note != null) {
                mainViewModel.insertNote(note)
                if (note.title != null){
                    val re = Regex("[^A-Za-z ]")
                    val child = re.replace(note.title,"")
                    note.title?.let { it1 -> reference.child(child).setValue(note) }
                }
            }

        }

        binding.trueFavIV.setOnClickListener {
            binding.falseFavIV.visibility = View.VISIBLE
            binding.trueFavIV.visibility = View.GONE

            val author = quotesList[mainViewModel.indexForQuotes].author
            val data = favList.firstOrNull { it.title == author }

            val note = data?.let { it1 ->
                Note(
                    id = it1.id,
                    title = quotesList[mainViewModel.indexForQuotes].author,
                    note = quotesList[mainViewModel.indexForQuotes].text,
                    date = formatter.format(Date()),
                    userName = Cons.userName,
                    favourite = false
                )
            }

            if (note != null) {
                mainViewModel.deleteNote(note)
                if (note.title != null){
                    val re = Regex("[^A-Za-z ]")
                    val child = re.replace(note.title,"")
                    note.title?.let { it1 -> reference.child(child).setValue(note) }
                }
            } else Log.d("selected not delete","error")
        }

    }

    private fun onObserve() {
        Cons.userName?.let {
            mainViewModel.getFavNote(it).observe(viewLifecycleOwner){
                favList.clear()
                favList.addAll(it)
                Log.d("quotes data 1","$it")
            }
        }

        mainViewModel.quotesLiveData.observe(viewLifecycleOwner) {
            Log.d("quotes data 2","$it")
            quotesList.clear()
            quotesList.addAll(it)
            if (quotesList != null){
                onSetup()
            }
        }

    }

}