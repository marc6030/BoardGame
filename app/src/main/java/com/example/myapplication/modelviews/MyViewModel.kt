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
import android.util.Log
import com.example.myapplication.models.BoardGameSearchItems
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class MyViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    private var _isBoardgameFavourite = MutableLiveData<Boolean>()
    private var _boardGameData = MutableLiveData<BoardGame?>()
    private var _boardGameList = MutableLiveData<BoardGameItems?>()
    private var _boardGameSearch = MutableLiveData<BoardGameSearchItems?>()
    private var _userAuthenticated = MutableLiveData<Boolean>()
    private var _favoriteBoardGameList = MutableLiveData<List<BoardGame?>>()

    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton
    //var favoriteBoardGameList: MutableState<List<BoardGame>> = mutableStateOf(emptyList())

    private lateinit var firebaseuser: FirebaseUser

    // Exposing the values for the views
    val db = FirebaseFirestore.getInstance()
    val isUserLoggedInGoogle: LiveData<Boolean> = _userAuthenticated
    val boardGameSearchResults: LiveData<BoardGameSearchItems?> = _boardGameSearch

    var isLoading: LiveData<Boolean> = _isLoading
    var boardGameDataList: LiveData<BoardGameItems?> = _boardGameList
    var boardGameData: LiveData<BoardGame?> = _boardGameData
    var favoriteBoardGameList: LiveData<List<BoardGame?>> = _favoriteBoardGameList
    var isBoardGameFavourite: LiveData<Boolean> = _isBoardgameFavourite

    fun addBoardGameItemToFavoriteList(boardGame: BoardGame) {
        // _favoriteBoardGameList.value = _favoriteBoardGameList.value?.plus(boardGame)
        insertIntoUserFavoriteDB(boardGame.id)
    }

    fun removeBoardGameItemToFavoriteList(boardGame: BoardGame) {
        removeFromUserFavoriteDB(boardGame.id)
    }

    fun itemExistsInFavoriteList(boardGame: BoardGame): Boolean {

        val favorites = _favoriteBoardGameList.value?: return false

        for (favoritedBoardGame in favorites) {
            val favboard = favoritedBoardGame?: return false
                if (favboard.id == boardGame.id) {
                    return true
                }
            }
        return false
    }


    fun toggleFavorite(boardGame: BoardGame?) { // Den er funktion tilføjer og fjerner items fra favorite listen
        if (boardGame != null) {
            if (itemExistsInFavoriteList(boardGame)) {
                removeBoardGameItemToFavoriteList(boardGame)
            } else {
                addBoardGameItemToFavoriteList(boardGame)
            }
        }
    }



    fun fetchBoardGameList() {             // Lige nu er det hot listen
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGameList: BoardGameItems = repository.getBoardGameList()

                withContext(Dispatchers.Main) {
                    _boardGameList.postValue(boardGameList)
                }
            } catch (e: Exception) {
                _boardGameList.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun insertIntoUserFavoriteDB(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val gameID = hashMapOf("id" to id)

                // Perform Firestore operation and wait for it to complete
                db.collection("BBUsers").document(getUserID())
                    .collection("favorites")
                    .document(id).set(gameID, SetOptions.merge()).await()

                // Fetch the BoardGame details after adding to Firestore
                val newFav = repository.getBoardGame(id)

                // Update the LiveData on the main thread
                withContext(Dispatchers.Main) {
                    // Use plus to add the new favorite BoardGame to the list
                    _favoriteBoardGameList.value = _favoriteBoardGameList.value?.plus(newFav)
                }
            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error in insertIntoUserFavoriteDB", e)
                // Write something handling exceptins exception
            }
        }
    }


    fun removeFromUserFavoriteDB(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("BBUsers").document(getUserID())
                    .collection("favorites")
                    .document(id).delete()
                    .await()
                withContext(Dispatchers.Main) {
                    // We are using filter instead of minus because minus compares the objects hash value which might differ
                    _favoriteBoardGameList.value = _favoriteBoardGameList.value?.filter { it!!.id != id } // "it" is a lambda function and checks all elements in the lsit..
                }
            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error writing document", e)
                // Write something handling exceptins exception
            }
        }
    }



    fun fetchFavoriteListFromDB() {


        val tempBg : ArrayList<BoardGame> = ArrayList();
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favSnapshot = db.collection("BBUsers").document(getUserID())
                    .collection("favorites")
                    .get()
                    .await()

                for (document in favSnapshot) {
                    val boardGame: BoardGame = repository.getBoardGame(document.id)
                    tempBg.add(boardGame)
                }


                //_boardGameData.postValue(boardGame)
            } catch (e: Exception) {
                _favoriteBoardGameList.postValue(null)
            } finally {
                _isLoading.postValue(false)
            }
            _favoriteBoardGameList.postValue(tempBg)
        }
    }


    fun fetchBoardGameData(id: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = repository.getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                if (favoriteBoardGameList.value?.contains(boardGame)!!) {
                    boardGame.isfavorite = true
                }
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
    fun setUser(firebaseUser: FirebaseUser) {
        this.firebaseuser = firebaseUser
        _userAuthenticated.value = true
    }

    fun getUserEmail(): String? {
        val email: String? = this.firebaseuser.email
        Log.v("User email is: ", "$email")
        return email
    }

    fun getUserID(): String {
        val userID: String = this.firebaseuser.uid
        Log.v("UserID is: ", userID)
        return userID
    }


    // LiveData to observe the user's sign-in status
    fun verifySignedIn(): LiveData<Boolean> {
        return _userAuthenticated
    }
}




