package com.example.myapplication.modelviews

import android.content.Context
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class MyViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    private var _boardGameList = MutableLiveData<BoardGameItems?>()
    private var _boardGameSearch = MutableLiveData<BoardGameSearchItems?>()
    private var _userAuthenticated = MutableLiveData<Boolean>()
    private var _firebaseuser = MutableLiveData<FirebaseUser?>()
    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton

    // Exposing the values for the views
    val db = FirebaseFirestore.getInstance()
    val isUserLoggedInGoogle: LiveData<Boolean> = _userAuthenticated
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


    fun checkCurrentUser(context: Context) : Boolean {
        // Check if the user is logged in and update _isUserLoggedIn
        // For example, you might check this from your AuthenticationManager
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            return true;
        }
        return false;
    }




    // Updates the FirebaseUser and the user authentication status
    fun setUser(firebaseUser: FirebaseUser?) {
        _firebaseuser.value = firebaseUser
        _userAuthenticated.value = firebaseUser != null
    }

    // LiveData to observe the user's sign-in status
    fun verifySignedIn(): LiveData<Boolean> {
        return _userAuthenticated
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


