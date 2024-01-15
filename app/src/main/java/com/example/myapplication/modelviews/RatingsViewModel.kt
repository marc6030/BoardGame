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


class RatingsViewModel(private var sharedViewModel: SharedViewModel) : ViewModel() {

    var ratedGamesList by mutableStateOf<List<BoardGameItem>>(emptyList())

    var averageRating by mutableStateOf(0.0)

    var offset = 0
    private var limit = 10

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }

    fun fetchRatedBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset = 0
                ratedGamesList = BoardGameRepository().getRatedGames(
                    UserID = getUserID(),
                    limit = limit,
                    offset = offset
                )
                Log.v("Fetch Rated ", "success!")
            } catch (e: Exception) {
                Log.v("Fetch Rated failed!: ", "$e")
            }
        }
    }

    fun fetchAdditionalRatedBoardGames() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                ratedGamesList += BoardGameRepository().getRatedGames(
                    getUserID(),
                    limit = limit,
                    offset = offset
                )
            } catch (e: Exception) {
                Log.v(
                    "fetchAdditionalRatedBoardGame",
                    "Can't fetch additional rated boardGames"
                )
            }
        }
    }

    fun fetchAverageRating(gameID : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                averageRating = BoardGameRepository().fetchAverageBbRating(gameID)
            }
            catch (e : Exception){
                Log.v("Cant fetch rating", "$e")
            }
        }
    }



}
