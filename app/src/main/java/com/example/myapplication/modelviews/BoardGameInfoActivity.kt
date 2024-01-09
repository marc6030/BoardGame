package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardGameInfoActivity() : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)

    var boardGameData by mutableStateOf(BoardGame())
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var isAtive by mutableStateOf(false)
    private var db = FirebaseFirestore.getInstance()

    var currentGameID = ""


    fun fetchBoardGameData(id: String) {
        // setIsLoading(true)
        currentGameID = id
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = BoardGameRepository().getBoardGame(id)

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