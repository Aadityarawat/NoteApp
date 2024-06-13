package com.example.noteapp.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title: String? = null,
    val note: String? = null,
    val date: String? = null,
    val userName : String? = null,
    var favourite : Boolean? = null
)
