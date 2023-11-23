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
import androidx.compose.runtime.MutableState
import com.example.myapplication.models.BoardGameSearchItems
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions


class MyViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    private var _boardGameList = MutableLiveData<BoardGameItems?>()
    private var _boardGameSearch = MutableLiveData<BoardGameSearchItems?>()
    private var _userAuthenticated = MutableLiveData<Boolean>()
    private var _firebaseuser = MutableLiveData<FirebaseUser?>()
    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton
    var favoriteBoardGameList: MutableState<List<BoardGame>> = mutableStateOf(emptyList())

    // Exposing the values for the views
    val db = FirebaseFirestore.getInstance()
    val isUserLoggedInGoogle: LiveData<Boolean> = _userAuthenticated
    val boardGameSearchResults: LiveData<BoardGameSearchItems?> = _boardGameSearch

    var isLoading: LiveData<Boolean> = _isLoading
    var boardGameDataList: LiveData<BoardGameItems?> = _boardGameList
    var boardGameData: LiveData<BoardGame?> = _boardGameData


    fun addBoardGameItemToFavoriteList(boardGame: BoardGame) {
        val currentList = favoriteBoardGameList.value.toMutableList()
        currentList.add(boardGame)
        favoriteBoardGameList.value = currentList
        insertIntoUserFavoriteDB(boardGame.id)
    }

    fun removeBoardGameItemToFavoriteList(boardGame: BoardGame) {
        val currentList = favoriteBoardGameList.value.toMutableList()
        currentList.remove(boardGame)
        favoriteBoardGameList.value = currentList
        removeFromUserFavoriteDB(boardGame.id)
    }

    fun itemExistsInFavoriteList(boardGame: BoardGame): Boolean {
        for (favoritedBoardGame in favoriteBoardGameList.value) {
            if (favoritedBoardGame.id == boardGame.id) {
                return true
            }
        }
        return false
    }

    fun toggleFavorite(boardGame: BoardGame) { // Den er funktion tilføjer og fjerner items fra favorite listen
        if (itemExistsInFavoriteList(boardGame)) {
            removeBoardGameItemToFavoriteList(boardGame)
        } else {
            addBoardGameItemToFavoriteList(boardGame)
        }
    }

    fun fetchBoardGameList() {             // Lige nu er det hot listen
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // val boardGameList: BoardGameItems = getDataAsBoardGameList(url) // Den her kan måske slettes, hvis den ikke bruges længere?
                val boardGameList: BoardGameItems = repository.getBoardGameList()
                _boardGameList.postValue(boardGameList)
            } catch (e: Exception) {
                _boardGameList.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun insertIntoUserFavoriteDB(id: String) {
        val gameID = hashMapOf(
            "id" to id
        )
        db.collection("BBUsers").document("USERHERE")
            .collection("favorites")
            .document(id).set(gameID, SetOptions.merge())
            .addOnSuccessListener {
                Log.v(
                    "FirebaseTest",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e -> Log.v("FirebaseTest", "Error writing document", e) }
    }

    fun removeFromUserFavoriteDB(id: String) {

        db.collection("BBUsers").document("USERHERE")
            .collection("favorites")
            .document(id).delete()
            .addOnSuccessListener {
                Log.v(
                    "FirebaseTest",
                    "DocumentSnapshot successfully written!"
                )
            }
            .addOnFailureListener { e -> Log.v("FirebaseTest", "Error writing document", e) }
    }

    fun getFavoriteList(user: String) {
        val currentList = favoriteBoardGameList.value.toMutableList()
        db.collection("BBUsers").document(user)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                }
                favoriteBoardGameList.value = currentList
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
                    val boardGameSearchItems: BoardGameSearchItems =
                        repository.getBoardGameSearch(userSearch)
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


        fun checkCurrentUser(context: Context): Boolean {
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
}




