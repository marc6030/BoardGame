package com.example.myapplication.API

import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("xmlapi/boardgame/{id}?stats=1")
    suspend fun getBoardGameXml(
        @Path("id") id: String
    ): Response<String>

    @GET("xmlapi2/hot?boardgame")
    suspend fun getBoardGameList(): Response<String>

    @GET("xmlapi2/search")
    suspend fun getBoardGameSearch(@Query("query") userSearch: String): Response<String>

}