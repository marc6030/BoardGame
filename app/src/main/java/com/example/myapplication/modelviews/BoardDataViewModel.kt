package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItem
import com.example.myapplication.models.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardDataViewModel(private var sharedViewModel: SharedViewModel) : ViewModel(){
    var boardGamesRow0 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow1 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow2 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow3 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow4 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var boardGamesRow5 by mutableStateOf<List<BoardGameItem>>(emptyList())
    var bigPictureGame by mutableStateOf(BoardGame())

    var categoryColumn by mutableStateOf<List<BoardGameItem>>(emptyList())

    var categories by mutableStateOf(Categories(emptyList()))


    var boardGamesRowRecent by mutableStateOf<List<BoardGameItem>>(emptyList())

    var streak by mutableStateOf("0")
    var nrOfPlayedGames by mutableStateOf("0")
    var nrOfRatedGames by mutableStateOf("0")
    var nrOfLikedGames by mutableStateOf("0")

    var bronzeRank by mutableStateOf(false)
    var silverRank by mutableStateOf(false)
    var goldRank by mutableStateOf(false)
    var platRank by mutableStateOf(false)

    private val categoryRow0 = null
    private val categoryRow1 = "fighting"
    private val categoryRow2 = "Economic"
    private val categoryRow3 = "Card game"
    private val categoryRow4 = "fantasy"
    private val categoryRow5 = "racing"

    private var offsetRow0 = 0
    private var offsetRow1 = 0
    private var offsetRow2 = 0
    private var offsetRow3 = 0
    private var offsetRow4 = 0
    private var offsetRow5 = 0

    private var offsetColumnCategory = 0


    private var limit = 10

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoriesAll: Categories = BoardGameRepository().getAllCategories()
                categories = categoriesAll

            } catch (e: Exception) {
                Log.v("Couldn't fetch all categories", "searchlogs: ${categories}")
                // boardGameSearch = null
            }
        }
    }

    fun fetchBoardGameCategory(category : String) {

        offsetColumnCategory = 0

        viewModelScope.launch(Dispatchers.IO) {
            try {
                categoryColumn =
                    BoardGameRepository().getBoardGameList(limit = limit, offset = offsetColumnCategory, category, sharedViewModel.getUserID())
            } catch (e: Exception) {
                Log.v("Cant fetch GameCategory", "$e")
            }
        }
    }
    fun fetchAdditionalBoardGameCategory(category : String) {
        offsetColumnCategory += limit
        viewModelScope.launch(Dispatchers.IO) {
            try {
                categoryColumn +=
                    BoardGameRepository().getBoardGameList(limit = limit, offset = offsetColumnCategory, category, sharedViewModel.getUserID())
            } catch (e: Exception) {
                Log.v("Cant fetch additional GAMECATEGORY", "$e")
            }
        }
    }

    fun fetchBoardGameCategories() {
        offsetRow0 = 0
        offsetRow1 = 0
        offsetRow2 = 0
        offsetRow3 = 0
        offsetRow4 = 0
        offsetRow5 = 0

        viewModelScope.launch(Dispatchers.IO) {
            try {
                bigPictureGame = BoardGameRepository().getBoardGame("316554", sharedViewModel.getUserID())
                boardGamesRow0 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow0, sharedViewModel.getUserID())
                boardGamesRow1 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow1, sharedViewModel.getUserID())
                boardGamesRow2 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow2, sharedViewModel.getUserID())
                boardGamesRow3 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow3, sharedViewModel.getUserID())
                boardGamesRow4 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow4, sharedViewModel.getUserID())
                boardGamesRow5 = BoardGameRepository().getBoardGameList(limit = limit, offset = 0, categoryRow5, sharedViewModel.getUserID())
            } catch (e: Exception) {
                Log.v("Cant fetch GameCategories", "$e")
            }
        }
    }

    fun fetchRecentBoardGames(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                boardGamesRowRecent = BoardGameRepository().getBoardGameToRecentList(sharedViewModel.getUserID())
            } catch (e: Exception) {
                Log.v("Cant fetch recentGames", "$e")
            }
        }
    }

    fun fetchKeyStats(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = BoardGameRepository().getNumberOfGamesAndStreak(sharedViewModel.getUserID())
                streak = user[0].streak
                nrOfPlayedGames = user[0].playedGames
                nrOfRatedGames = user[0].ratedGames
                nrOfLikedGames = user[0].likedGames
            } catch (e: Exception) {
                Log.v("Cant fetch keystats", "$e")
            }
        }
    }




    fun fetchAdditionalBoardGameCategories(row: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (row == 0) {
                    offsetRow0 += limit
                    boardGamesRow0 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow0, categoryRow0, sharedViewModel.getUserID())
                } else if (row == 1) {
                    offsetRow1 += limit
                    boardGamesRow1 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow1, categoryRow1, sharedViewModel.getUserID())
                } else if (row == 2) {
                    offsetRow2 += limit
                    boardGamesRow2 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow2, categoryRow2, sharedViewModel.getUserID())
                } else if (row == 3) {
                    offsetRow3 += limit
                    boardGamesRow3 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow3, categoryRow3, sharedViewModel.getUserID())
                } else if (row == 4) {
                    offsetRow4 += limit
                    boardGamesRow4 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow4, categoryRow4, sharedViewModel.getUserID())
                } else if (row == 5) {
                    offsetRow5 += limit
                    boardGamesRow5 += BoardGameRepository().getBoardGameList(limit = limit, offset = offsetRow5, categoryRow5, sharedViewModel.getUserID())
                }
            } catch (e: Exception) {
                Log.v("fetchAdditionalBoardGameCategories","Can't fetch additional boardGameCategories")
            }
        }
    }

}