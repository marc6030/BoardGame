package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.models.BoardGameSearchItems
import com.example.myapplication.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BoardSearchViewModel(private var sharedViewModel: SharedViewModel) : ViewModel() {
    var boardGameSearch by mutableStateOf<BoardGameSearchItems?>(null)

    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }

    fun fetchGameBoardSearch(userSearch: String) {
        setIsLoading(true)
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
                setIsLoading(false)
            }
        }
    }
}