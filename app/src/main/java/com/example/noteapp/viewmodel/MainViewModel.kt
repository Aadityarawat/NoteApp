package com.example.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.repository.UserRepository
import com.example.noteapp.room.entities.Note
import com.example.noteapp.room.entities.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    var indexForQuotes = 0
    val quotesLiveData get() = repository.quotesLiveData
    //val allNotes : LiveData<List<Note>> = repository.getNote()
    lateinit var noteData : Note

    fun getUser() : LiveData<List<User>>{
        return repository.getUser()
    }

    fun insertUser(user: User){
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun getNote(username : String) : LiveData<List<Note>>{
        return repository.getNote(username)
    }

    fun insertNote(note: Note){
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun getQuotes(){
        viewModelScope.launch {
            repository.getQuotes()
        }
    }

    fun getFavNote(username: String) : LiveData<List<Note>>{
        return repository.getFavNotes(username)
    }
}