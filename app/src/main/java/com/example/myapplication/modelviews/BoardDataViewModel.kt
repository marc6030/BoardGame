package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import com.example.myapplication.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardDataViewModel : ViewModel(){
    var isLoading by mutableStateOf(false)
    var boardGameData by mutableStateOf<BoardGame?>(null)
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())

    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton


    fun fetchBoardGameList() {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                boardGameList = repository.getBoardGameList()
                withContext(Dispatchers.Main) {}
            } catch (e: Exception) {
                boardGameList = null
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchBoardGameData(id: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = repository.getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                if (favoriteBoardGameList.any { it?.id == boardGame.id }) {
                    boardGame.isfavorite = true
                }
                withContext(Dispatchers.Main) {
                    boardGameData = boardGame
                }
            } catch (e: Exception) {
                boardGameData = null
            } finally {
                isLoading = false
            }
        }
    }


}