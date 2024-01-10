package com.example.myapplication.modelviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.BoardGame

class FavoriteViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {


    var favoriteBoardGameList by mutableStateOf<List<BoardGame>>(emptyList())

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }



}