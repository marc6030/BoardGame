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

    fun toggleRatings(boardGame: BoardGame?, rating: String) {
        if (boardGame != null) {
            if (boardGame.userRating != rating) {
                insertAverageRating(boardGame.id, rating)
                fetchAverageRating(boardGame)
                fetchUserRating(boardGame)
            } else {
                removeRatingFromDB(boardGame.id)
                fetchAverageRating(boardGame)
                fetchUserRating(boardGame)
            }
            _boardGameData.value =
                boardGame// Assuming _boardGameData is the MutableState
        }
    }

    fun toggleFavorite(boardGame: BoardGame?) {
        if (boardGame != null) {
            val updatedBoardGame = boardGame.copy(isfavorite = !boardGame.isfavorite)
            if (updatedBoardGame.isfavorite) {
                insertIntoUserFavoriteDB(boardGame.id)
            } else {
                removeFromUserFavoriteDB(boardGame.id)
            }
            _boardGameData.value = updatedBoardGame // Assuming _boardGameData is the MutableState
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

                    Log.v("add fav list", "${favoriteBoardGameList.value?.map { it?.id }}")
                }
            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error in insertIntoUserFavoriteDB", e)
                // Write something handling exceptins exception
            }
        }
    }

    fun insertAverageRating(id: String, rating: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userRating = hashMapOf("rating" to rating)

                db.collection("Ratings").document(id)
                    .collection("userRatings").document(getUserID())
                    .set(userRating, SetOptions.merge())
                    .await()

            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error in insertAverageRating", e)
                // Write something handling exceptins exception
            }
        }
    }

    fun fetchAverageRating(boardGame: BoardGame) {
        var average = 0
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratingSnapshot = db.collection("Ratings")
                    .document(boardGame.id)
                    .collection("userRatings")
                    .get()
                    .await()

                var rating = 0

                for (document in ratingSnapshot.documents) {
                    val ratingString: String = document["rating"].toString()
                    rating += ratingString.toInt()
                }

                withContext(Dispatchers.Main) {
                    if (ratingSnapshot.documents.size != 0) {
                        average = rating / ratingSnapshot.documents.size
                    }
                    boardGame.averageRatingBB = average
                    Log.v(
                        "add fav list",
                        "${average} + ${ratingSnapshot.documents.size}"
                    )
                }
            } catch (e: Exception) {
                Log.e("fetchAverageRating", "Error fetching average rating", e)
            }
        }
}

    fun fetchUserRating(boardGame: BoardGame){
        var ratingString: String = ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratingSnapshot = db.collection("Ratings")
                    .document(boardGame.id)
                    .collection("userRatings")
                    .document(getUserID()).get()
                    .await()
                if(ratingSnapshot != null) {
                    ratingString = ratingSnapshot["rating"].toString()
                }
                withContext(Dispatchers.Main) {
                    boardGame.userRating = ratingString
                    Log.v("get userRating", "${ratingString} + ${ratingSnapshot["rating"].toString()}")
                }
            } catch (e: Exception) {
                Log.e("fetchAverageRating", "Error fetching average rating", e)
            }
        }
    }


    fun removeRatingFromDB(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("Ratings").document(id)
                    .collection("userRatings")
                    .document(getUserID())
                    .delete()
                    .await()

            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error writing document", e)
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
                    _favoriteBoardGameList.value =
                        _favoriteBoardGameList.value?.filter { it!!.id != id } // "it" is a lambda function and checks all elements in the list..
                    Log.v("remove fav list", "${favoriteBoardGameList.value?.map { it?.id }}")
                }
            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error writing document", e)
                // Write something handling exceptions exception
            }
        }
    }


    fun fetchFavoriteListFromDB() {
        val tempBg: ArrayList<BoardGame> = ArrayList();
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favSnapshot = db.collection("BBUsers").document(getUserID())
                    .collection("favorites")
                    .get()
                    .await()

                for (document in favSnapshot) {
                    val boardGame: BoardGame = repository.getBoardGame(document.id)
                    boardGame.isfavorite = true
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
                if (favoriteBoardGameList.value!!.any { it?.id == boardGame.id }) {
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




