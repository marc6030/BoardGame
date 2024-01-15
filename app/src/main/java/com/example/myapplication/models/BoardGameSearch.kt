package com.example.myapplication.models


data class BoardGameSearchItems(
    var boardGameSearchItems: MutableList<BoardGameSearch> = mutableListOf()
)


data class BoardGameSearch(

    var name: String = "",

    var id: String = "",

    var imgUrl: String = "",

    var description: String = ""
)
