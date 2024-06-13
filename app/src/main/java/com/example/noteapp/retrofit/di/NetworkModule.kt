package com.example.noteapp.retrofit.di

import com.example.noteapp.others.Cons
import com.example.noteapp.retrofit.api.QuotesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Cons.QUOTES_BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providesQuotesApi(retrofit: Retrofit) : QuotesApi{
        return retrofit.create(QuotesApi::class.java)
    }
}