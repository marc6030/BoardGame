package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.BoardGameItems
import com.example.myapplication.models.BoardGameSearchItems
import com.example.myapplication.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardSearchViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var boardGameSearch by mutableStateOf<BoardGameSearchItems?>(null)

    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton


    fun fetchGameBoardSearch(userSearch: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGameSearchItems: BoardGameSearchItems = repository.getBoardGameSearch(userSearch)
                Log.v("bgsearch", "searchlogs: $boardGameSearchItems")
                withContext(Dispatchers.Main) {
                    boardGameSearch = boardGameSearchItems
                }
            } catch (e: Exception) {
                Log.v("bgsearch", "searchlogs: $e")
                boardGameSearch = null
            } finally {
                isLoading = false
            }
        }
    }
}