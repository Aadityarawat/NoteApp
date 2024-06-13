package com.example.noteapp.room.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteapp.room.entities.Note
import com.example.noteapp.room.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("Select * from user")
    fun getUser() : LiveData<List<User>>

    @Insert
    suspend fun insertNote(note : Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("update notes_table set title = :title, note = :note, date = :date, favourite = :favourite where id = :id")
    suspend fun updateNote(id : Int?, title : String?, note: String?, date: String?, favourite: Boolean?)

    @Query("Select * from notes_table where userName = :username order by id ASC")
    fun getNotes(username : String) : LiveData<List<Note>>

    @Query("Select * from notes_table where userName = :username AND favourite = :favourite order by id ASC")
    fun getFavNotes(username: String, favourite: Boolean) : LiveData<List<Note>>
}