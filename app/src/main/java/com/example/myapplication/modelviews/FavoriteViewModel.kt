package com.example.myapplication.modelviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.BoardGame
import com.example.myapplication.repositories.postgresql
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {


    private var db = FirebaseFirestore.getInstance()
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())
    

    fun toggleFavorite(gameID: String) {
        postgresql().setFavoriteGame("static_user", gameID)
    }

}