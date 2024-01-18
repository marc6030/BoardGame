package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardGameInfoActivity(private var sharedViewModel: SharedViewModel) : ViewModel() {

    var boardGameData by mutableStateOf(BoardGame())

    var openRatingPopUp by mutableStateOf(false)
    var snackbarFavoriteVisible by mutableStateOf(false)
    var snackbarChallengeVisible by mutableStateOf(false)

    var youtubeID by mutableStateOf("")

    var currentGameID = ""

    var averageRating by mutableStateOf(0.0)

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }

    fun toggleFavorite(gameID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (boardGameData.liked == "True"){
                    boardGameData.liked = "False"
                } else {
                    boardGameData.liked = "True"
                }

                BoardGameRepository().toggleFavoriteGame(getUserID(), gameID)
            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
            }
        }
    }

    fun addToRecentBoardGames(boardGameId : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BoardGameRepository().addBoardGameToRecentList(getUserID(), boardGameId)
            } catch (e: Exception) {
                Log.v("Cant add to recentGames", "$e")
            }
        }
    }



    fun fetchBoardGameData(id: String) {
        currentGameID = id

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = BoardGameRepository().getBoardGame(id, getUserID())
                boardGameData = boardGame
                averageRating = BoardGameRepository().fetchAverageBbRating(boardGameData.id)
            } catch (e: Exception) {
                Log.v("can't fetch boardgamedata: ", "$e")
            } finally {
                //
            }
        }
    }

    fun fetchYoutubeID(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val youtubeID_ = BoardGameRepository().searchYoutube(id + "boardgame")

                withContext(Dispatchers.Main) {
                    youtubeID = youtubeID_
                }
            } catch (e: Exception) {
                Log.v("can't fetch youtubeID: ", "$e")
            } finally {
                //
            }
        }
    }

    fun updateRating(newRating: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BoardGameRepository().toggleRatingGame(getUserID(), boardGameData.id, newRating)
                fetchBoardGameData(boardGameData.id)
            } catch (e: Exception) {
                Log.v("RatingUpdate", "RatingErrorUpdateMessage: $e")
            }
        }
    }

    fun addOrRemovePlayedGames(gameID : String, increment : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BoardGameRepository().addOrRemovePlayedGame(getUserID(), gameID, increment)
            }
            catch (e : Exception){
                Log.v("update Played games failed!: ", "$e")
            }
        }
    }
}