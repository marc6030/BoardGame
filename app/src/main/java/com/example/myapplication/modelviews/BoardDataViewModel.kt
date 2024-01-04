package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.BoardGame
import com.example.myapplication.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardDataViewModel(private var sharedViewModel: SharedViewModel) : ViewModel(){

    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton
    var backgroundFade by mutableStateOf(Color.Black)

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }

    fun fetchBoardGameList() {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sharedViewModel.boardGameList = repository.getBoardGameList()
                withContext(Dispatchers.Main) {}
            } catch (e: Exception) {
                sharedViewModel.boardGameList = null
            } finally {
                setIsLoading(false)
            }
        }
    }

    fun fetchBoardGameData(id: String) {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = repository.getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                if (sharedViewModel.favoriteBoardGameList.any { it?.id == boardGame.id }) {
                    boardGame.isfavorite = true
                }
                withContext(Dispatchers.Main) {
                    sharedViewModel.boardGameData = boardGame
                }
            } catch (e: Exception) {
                sharedViewModel.boardGameData = null
            } finally {
                setIsLoading(false)
            }
        }
    }
}