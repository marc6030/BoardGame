package com.example.myapplication.modelviews

import BoardGameRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.BoardGame

class FavoriteViewModel() : ViewModel() {


    var favoriteBoardGameList by mutableStateOf<List<BoardGame>>(emptyList())


    fun toggleFavorite(gameID: String) {
        BoardGameRepository().toggleFavoriteGame("static_user", gameID)
    }

}