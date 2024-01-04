package com.example.myapplication.modelviews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.repositories.postgresql
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardDataViewModel(private var sharedViewModel: SharedViewModel) : ViewModel(){

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }

    fun fetchBoardGameList() {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sharedViewModel.boardGameList = postgresql().getBoardGameList()
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
                val boardGame: BoardGame = postgresql().getBoardGame(id)
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