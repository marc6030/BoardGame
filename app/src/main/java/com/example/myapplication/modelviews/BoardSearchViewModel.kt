package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.BoardGameSearch
import com.example.myapplication.models.BoardGameSearchItems
import com.example.myapplication.repositories.postgresql
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BoardSearchViewModel(private var sharedViewModel: SharedViewModel) : ViewModel() {
    // var boardGameSearch by mutableStateOf(BoardGameSearchItems())
    var boardGameSearch by mutableStateOf<List<BoardGameSearch>>(emptyList())
    var input by mutableStateOf("")
    var limit by mutableIntStateOf(30) // could be a value
    var offset by mutableIntStateOf(0) // could be a value

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }

    fun fetchGameBoardSearch() {
        if (input === "") {
            return
        }
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset = 0
                val boardGameSearchItems: BoardGameSearchItems = postgresql().getBoardGameSearch(input, limit, offset)
                Log.v("bgsearch", "searchlogs: $boardGameSearchItems")
                boardGameSearch = boardGameSearchItems.boardGameSearchItems

            } catch (e: Exception) {
                Log.v("bgsearch_fault", "searchlogs: $e")
                // boardGameSearch = null
            } finally {
                setIsLoading(false)
            }
        }
    }

    fun fetchAdditionalSearchResults() {
        setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                offset += limit
                val newBoardGameSearchItems: BoardGameSearchItems = postgresql().getBoardGameSearch(input, limit, offset)
                boardGameSearch += newBoardGameSearchItems.boardGameSearchItems

                Log.v("bgsearch_new", "searchlogs: $newBoardGameSearchItems")
                Log.v("bgsearch_new_all", "$boardGameSearch")


            } catch (e: Exception) {
                Log.v("bgsearch_new_fault", "searchlogs: $boardGameSearch")
                // boardGameSearch = null
            } finally {
                setIsLoading(false)
            }
        }
    }
}