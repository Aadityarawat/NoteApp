package com.example.noteapp.retrofit.model

import com.google.gson.annotations.SerializedName

data class Quotes(
    @SerializedName("text")
    val text: String?,
    @SerializedName("author")
    val author: String?
)
