package com.example.myapplication.API

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

// We have moved to Retrofit because it's part of the lectures
// We have implemented a singleton pattern for the connection to lessen resource waste

// objects is pr definition singleton in kotlin... Sorcery... Pure sorcery...
object RetrofitClient {

    private const val BASE_URL = "https://boardgamegeek.com/"

    // Lazy initialization of Retrofit, it's thread-safe by default
    val instance: ApiService by lazy {
        // Create Retrofit builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Ensure that Retrofit can handle plain text responses
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()


        // Create an implementation of the API endpoints defined by the ApiService interface
        retrofit.create(ApiService::class.java)
    }
}
