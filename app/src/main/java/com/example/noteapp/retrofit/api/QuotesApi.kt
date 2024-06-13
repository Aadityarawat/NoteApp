package com.example.noteapp.retrofit.api

import com.example.noteapp.retrofit.model.Quotes
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("quotes")
    suspend fun getQuotes() : Response<List<Quotes>>
}