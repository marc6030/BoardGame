package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems

class SharedViewModel : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var userAuthenticated by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    var boardGameData by mutableStateOf(BoardGame())
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())

    var currentGameID = ""

    fun getUserID(): String {
        val userID: String = "static_user"
        Log.v("UserID is: ", userID)
        return userID
    }

}