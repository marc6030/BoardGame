package com.example.myapplication.modelviews

import com.example.myapplication.repositories.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import com.example.myapplication.models.BoardGameSearchItems


class MyViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    private var _boardGameList = MutableLiveData<BoardGameItems?>()
    private var _boardGameSearch = MutableLiveData<BoardGameSearchItems?>()
    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton

    // Exposing the values for the views
    val boardGameSearchResults: LiveData<BoardGameSearchItems?> = _boardGameSearch

    var isLoading: LiveData<Boolean> = _isLoading
    var boardGameDataList: LiveData<BoardGameItems?> = _boardGameList
    var boardGameData: LiveData<BoardGame?> = _boardGameData


    fun fetchBoardGameList() {             // Lige nu er det hot listen
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // val boardGameList: BoardGameItems = getDataAsBoardGameList(url)
                val boardGameList: BoardGameItems = repository.getBoardGameList()
                _boardGameList.postValue(boardGameList)
            } catch (e: Exception) {
                _boardGameList.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }


    fun fetchBoardGameData(id: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = repository.getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                _boardGameData.postValue(boardGame)
            } catch (e: Exception) {
                _boardGameData.postValue(null)
            } finally {
                _isLoading.postValue(false) // why?
            }
        }
    }

    fun fetchGameBoardSearch(userSearch: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGameSearchItems: BoardGameSearchItems = repository.getBoardGameSearch(userSearch)
                Log.v("bgsearch", "searchlogs: $boardGameSearchItems")
                _boardGameSearch.postValue(boardGameSearchItems)
            } catch (e: Exception) {
                Log.v("bgsearch", "searchlogs: $e")
                _boardGameSearch.postValue(null)
            } finally {
                _isLoading.postValue(false) // why?
            }
        }
    }

    /*
    private fun fetchData(url: String): Deferred<String> {
        return mainScope.async {
            try {
                makeNetworkRequest(url)
            } catch (e: Exception) {
                ""
            }
        }
    }

    private suspend fun makeNetworkRequest(url: String): String {
        try {
            return withContext(Dispatchers.IO) {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string() ?: ""
                } else {
                    ""
                }
            }
        } catch (e: Exception) {
            println("Could not get response from BGG")
            return ""
        }
    }

     */
}


