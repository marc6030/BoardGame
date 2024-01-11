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

class TrackingViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {

    var playedGamesList by mutableStateOf<List<BoardGameItem>>(emptyList())

    var offset = 0
    private var limit = 10

    private fun getUserID(): String {
        return sharedViewModel.getUserID()
    }

    fun fetchPlayedBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset = 0
               /* playedGamesList = BoardGameRepository().getPlayedGames(
                    username = getUserID(),
                    limit = limit,
                    offset = offset
                )*/
                Log.v("Fetch Played Games ", "success!")
            } catch (e: Exception) {
                Log.v("Fetch Played games failed!: ", "$e")
            }
        }
    }
    fun fetchAdditionalFavoriteBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                /*playedGamesList += BoardGameRepository().getPlayedGames(
                    getUserID(),
                    limit = limit,
                    offset = offset
                )*/
            } catch (e: Exception) {
                Log.v(
                    "fetchAdditionalFavoriteBoardGame",
                    "Can't fetch additional favorite boardGames"
                )
            }
        }
    }
    fun addOrRemovePlayedGames(boardgame : BoardGameItem, add : Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(add)
                playedGamesList = playedGamesList.drop(playedGamesList.indexOf(boardgame))
                BoardGameRepository().toggleFavoriteGame(getUserID(), boardgame.id)
            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
                // boardGameSearch = null
            }
        }
    }

}