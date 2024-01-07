package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import com.example.myapplication.repositories.postgresql
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardDataViewModel(private var sharedViewModel: SharedViewModel) : ViewModel(){
    var boardGamesRow0 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow1 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow2 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow3 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow4 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow5 by mutableStateOf<List<BoardGameItem>>(emptyList())

    val categoryRow0 = null
    val categoryRow1 = "fighting"
    val categoryRow2 = "Economic"
    val categoryRow3 = "Card game"
    val categoryRow4 = "fantasy"
    val categoryRow5 = "racing"

    var offsetRow0 = 0
    var offsetRow1 = 0
    var offsetRow2 = 0
    var offsetRow3 = 0
    var offsetRow4 = 0
    var offsetRow5 = 0



    private var limit = 10

    var backgroundFade by mutableStateOf(Color.Black)

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }

    fun fetchBoardGameCategories() {
        offsetRow0 = 0
        offsetRow1 = 0
        offsetRow2 = 0
        offsetRow3 = 0
        offsetRow4 = 0
        offsetRow5 = 0
        // setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                boardGamesRow0 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow0)
                boardGamesRow1 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow1)
                boardGamesRow2 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow2)
                boardGamesRow3 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow3)
                boardGamesRow4 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow4)
                boardGamesRow5 = postgresql().getBoardGameList(limit = limit, offset = 0, categoryRow5)
            } catch (e: Exception) {
                sharedViewModel.boardGameList = null
            } finally {
                setIsLoading(false)
            }
        }
    }

    fun fetchAdditionalBoardGameCategories(row: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                if (row == 0) {
                    offsetRow0 += limit
                    boardGamesRow0 += postgresql().getBoardGameList(limit = limit, offset = offsetRow0, categoryRow0)
                } else if (row == 1) {
                    offsetRow1 += limit
                    boardGamesRow1 += postgresql().getBoardGameList(limit = limit, offset = offsetRow1, categoryRow1)
                } else if (row == 2) {
                    offsetRow2 += limit
                    boardGamesRow2 += postgresql().getBoardGameList(limit = limit, offset = offsetRow2, categoryRow2)
                } else if (row == 3) {
                    offsetRow3 += limit
                    boardGamesRow3 += postgresql().getBoardGameList(limit = limit, offset = offsetRow3, categoryRow3)
                } else if (row == 4) {
                    offsetRow4 += limit
                    boardGamesRow4 += postgresql().getBoardGameList(limit = limit, offset = offsetRow4, categoryRow4)
                } else if (row == 5) {
                    offsetRow5 += limit
                    boardGamesRow5 += postgresql().getBoardGameList(limit = limit, offset = offsetRow5, categoryRow5)
                }
            } catch (e: Exception) {
                Log.v("fetchAdditionalBoardGameCategories","Can't fetch additional boardGameCategories")
            }
        }
    }

    fun fetchBoardGameData(id: String) {
        // setIsLoading(true)
        sharedViewModel.currentGameID = id
        Log.v("kkk", "kkk")
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
                Log.v("can't fetch boardgamedata: ", "$e")
            } finally {
                setIsLoading(false)
            }
        }
    }
}