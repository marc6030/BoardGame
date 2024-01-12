package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGameItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {


    var favoriteBoardGameList by mutableStateOf<List<BoardGameItem>>(emptyList())
    var favoriteBoardGameListCheck by mutableStateOf(0)

    var offset = 0
    private var limit = 10
    private fun getUserID(): String {
        return sharedViewModel.getUserID()
    }

    fun fetchFavoriteBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset = 0
                favoriteBoardGameList = BoardGameRepository().getFavoriteGames(
                    username = getUserID(),
                    limit = limit,
                    offset = offset
                )
                Log.v("Fetch Favorites ", "success!")
            } catch (e: Exception) {
                Log.v("Fetch Favorites failed!: ", "$e")
            }
        }
    }

    fun fetchAdditionalFavoriteBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                favoriteBoardGameList += BoardGameRepository().getFavoriteGames(
                    getUserID(),
                    limit = limit,
                    offset = offset
                )
            } catch (e: Exception) {
                Log.v(
                    "fetchAdditionalFavoriteBoardGame",
                    "Can't fetch additional favorite boardGames"
                )
            }
        }
    }
    fun removeFavorite(boardgame : BoardGameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                favoriteBoardGameList = favoriteBoardGameList.drop(favoriteBoardGameList.indexOf(boardgame))
                favoriteBoardGameListCheck++
                BoardGameRepository().toggleFavoriteGame(getUserID(), boardgame.id)
            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
                // boardGameSearch = null
            }
        }
    }

}