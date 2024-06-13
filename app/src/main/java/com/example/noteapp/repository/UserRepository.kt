package com.example.noteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noteapp.retrofit.api.QuotesApi
import com.example.noteapp.retrofit.model.Quotes
import com.example.noteapp.room.database.UserDatabase
import com.example.noteapp.room.entities.Note
import com.example.noteapp.room.entities.User
import org.json.JSONObject
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDatabase: UserDatabase, private val quotesApi: QuotesApi){

    private val _quotesLiveData = MutableLiveData<List<Quotes>>()
    val quotesLiveData : LiveData<List<Quotes>> get() = _quotesLiveData

    fun getUser() : LiveData<List<User>>{
        return userDatabase.userDao().getUser()
    }

    suspend fun insertUser(user: User){
        userDatabase.userDao().insertUser(user)
    }

    suspend fun insertNote(note: Note){
        userDatabase.userDao().insertNote(note)
    }

    suspend fun deleteNote(note: Note){
        userDatabase.userDao().deleteNote(note)
    }

    fun getNote(username : String) : LiveData<List<Note>>{
        return userDatabase.userDao().getNotes(username)
    }

    suspend fun updateNote(note: Note){
        userDatabase.userDao().updateNote(note.id, note.title, note.note, note.date, note.favourite)
    }

    fun getFavNotes(username: String) : LiveData<List<Note>>{
        return userDatabase.userDao().getFavNotes(username, true)
    }

    suspend fun getQuotes(){
        val response = quotesApi.getQuotes()
        if (response.isSuccessful && response.body() != null){
            _quotesLiveData.postValue(response.body())
        }
//        else if (response.errorBody() != null){
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _quotesLiveData.postValue(response.errorBody())
//        }
    }
}