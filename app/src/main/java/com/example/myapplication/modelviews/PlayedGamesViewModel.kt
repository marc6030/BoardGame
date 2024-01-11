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

class PlayedGamesViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {

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
                playedGamesList = BoardGameRepository().getPlayedGames(
                    username = getUserID(),
                    limit = limit,
                    offset = offset
                )
                Log.v("Fetch Played Games ", "success!")
            } catch (e: Exception) {
                Log.v("Fetch Played games failed!: ", "$e")
            }
        }
    }
    fun fetchAdditionalPlayedBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                playedGamesList += BoardGameRepository().getPlayedGames(
                    getUserID(),
                    limit = limit,
                    offset = offset)
            } catch (e: Exception) {
                Log.v(
                    "fetchAdditionalFavoriteBoardGame",
                    "Can't fetch additional favorite boardGames"
                )
            }
        }
    }
    fun removeOrDecrementPlayedGames(boardgame : BoardGameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var newPlayedCount  : Int
                var updatedGameItem = playedGamesList.get(playedGamesList.indexOf(boardgame))
                playedGamesList = playedGamesList.drop(playedGamesList.indexOf(boardgame))
                newPlayedCount = updatedGameItem.playedCount.toInt()-1
                updatedGameItem.playedCount = newPlayedCount.toString()
                if(newPlayedCount != 0){
                    playedGamesList = playedGamesList.plus(updatedGameItem)
                    }
                boardGameInfoActivity.addOrRemovePlayedGames(boardgame.id, "False")
            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
                // boardGameSearch = null
            }
        }
    }


}