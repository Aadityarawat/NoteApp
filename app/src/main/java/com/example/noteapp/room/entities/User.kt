package com.example.noteapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val firstName : String? = null,
    val lastName : String? = null,
    val email : String? = null,
    val password : String? = null
)
