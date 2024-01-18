package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.BoardGameSearch
import com.example.myapplication.models.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardSearchViewModel() : ViewModel() {
    var boardGameSearch by mutableStateOf<List<BoardGameSearch>>(emptyList())
    var input by mutableStateOf("")
    var limit by mutableIntStateOf(15)
    var offset by mutableIntStateOf(0)
    var categories = mutableStateMapOf<String, Boolean>()



    fun fetchGameBoardSearch() {
        if (input === "") {
            boardGameSearch = emptyList()
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset = 0
                boardGameSearch = emptyList()
                boardGameSearch = BoardGameRepository().getBoardGameSearch(input, limit, offset, categories)

            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
            }
        }
    }

    fun fetchAdditionalSearchResults() {
        // setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                Log.v("kkkkk", "$offset")
                val newBoardGameSearchItems: List<BoardGameSearch> = BoardGameRepository().getBoardGameSearch(input, limit, offset, categories)
                boardGameSearch += newBoardGameSearchItems


            } catch (e: Exception) {
                Log.v("bgsearch_new_fault", "searchlogs: $boardGameSearch")
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val categoriesAll: Categories = BoardGameRepository().getAllCategories()
                for (category in categoriesAll.categories) {
                    categories[category] = false
                }
            } catch (e: Exception) {
                Log.v("bgsearch_new_fault", "searchlogs: $boardGameSearch")
            }
        }
    }
}