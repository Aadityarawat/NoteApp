package com.example.noteapp.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.room.Dao.UserDao
import com.example.noteapp.room.entities.Note
import com.example.noteapp.room.entities.User

@Database(entities = [User::class, Note::class], version = 1)
abstract class UserDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao
}