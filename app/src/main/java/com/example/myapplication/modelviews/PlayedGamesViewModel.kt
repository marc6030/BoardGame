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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayedGamesViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {

    var playedGamesList by mutableStateOf<List<BoardGameItem>>(emptyList())
    var playedGamesCheck by mutableStateOf(0)

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
                boardGameInfoActivity.addOrRemovePlayedGames(boardgame.id, "False")
                withContext(Dispatchers.Main) {
                    playedGamesCheck++
                }
            } catch (e: Exception) {
                Log.v("removeDecrement_fault", "RemoveDecrement: $e")
            }
        }
    }
    fun addOrIncrementPlayedGames(boardgame : BoardGameItem) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                boardGameInfoActivity.addOrRemovePlayedGames(boardgame.id, "True")
                withContext(Dispatchers.Main) {
                    playedGamesCheck++
                }
            } catch (e: Exception) {
                Log.v("addIncrement_fault", "AddIncrement: $e")
            }
        }
    }

}