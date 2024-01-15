package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {


    var favoriteBoardGameList by mutableStateOf<List<BoardGameItem>>(emptyList())

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }

    fun fetchFavoriteBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // favoriteBoardGameList = BoardGameRepository().getFavoritesList(limit = 50, offset = 0, getUserID())
                Log.v("Fetch Favorites ", "success!")
            } catch (e: Exception) {
                Log.v("Fetch Favorites failed!: ", "$e")
            }
        }
    }



}