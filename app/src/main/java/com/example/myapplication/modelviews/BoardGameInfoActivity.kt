package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import com.example.myapplication.repositories.postgresql
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardGameInfoActivity(private var favoriteViewModel: FavoriteViewModel) : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)

    var boardGameData by mutableStateOf(BoardGame())
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var isAtive by mutableStateOf(false)
    private var db = FirebaseFirestore.getInstance()

    var currentGameID = ""


    fun fetchBoardGameData(id: String) {
        // setIsLoading(true)
        currentGameID = id
        Log.v("kkk", "kkk")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = postgresql().getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                if (favoriteViewModel.favoriteBoardGameList.any { it?.id == boardGame.id }) {
                    boardGame.isfavorite = true
                }
                withContext(Dispatchers.Main) {
                    boardGameData = boardGame
                }
            } catch (e: Exception) {
                Log.v("can't fetch boardgamedata: ", "$e")
            } finally {
                //
            }
        }
    }


}