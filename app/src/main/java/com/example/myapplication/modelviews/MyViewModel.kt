package com.example.myapplication.modelviews

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.RetrofitClient
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import com.example.myapplication.models.BoardGameSearchItems
import com.example.myapplication.repositories.Repository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MyViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var boardGameData by mutableStateOf<BoardGame?>(null)
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var boardGameSearch by mutableStateOf<BoardGameSearchItems?>(null)
    var userAuthenticated by mutableStateOf(false)
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())
    var userRating by mutableStateOf("")
    var averageRating by mutableStateOf<Int?>(null)


    private val apiService by lazy { RetrofitClient.instance } // interface for connections... Is loaded on appstart and thus doesn't strictly needs to be lazy.
    private val repository = Repository(apiService) // factory builder and singleton

    private lateinit var firebaseuser: FirebaseUser

    // Exposing the values for the views
    lateinit var db: FirebaseFirestore

    fun setDB(fbinstance: FirebaseFirestore) {
        this.db = fbinstance
    }



    fun toggleRatings(boardGame: BoardGame?, rating: String) {
        if (boardGame != null) {
            if (boardGame.userRating != rating) {
                    insertAverageRating(boardGame.id, rating)
                }
            else {
                    removeRatingFromDB(boardGame.id)
                    boardGame.userRating = ""
                }
        fetchAverageRating(boardGame)
        fetchUserRating(boardGame)
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
            boardGameData = updatedBoardGame // Assuming _boardGameData is the MutableState
        }
    }

    fun fetchBoardGameList() {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                boardGameList = repository.getBoardGameList()
                withContext(Dispatchers.Main) {}
            } catch (e: Exception) {
                boardGameList = null
            } finally {
                isLoading = false
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
                    favoriteBoardGameList += newFav

                    Log.v("add fav list", "${favoriteBoardGameList}")
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
                    averageRating = average
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
                if(ratingSnapshot.get("rating") != null) {
                    ratingString = ratingSnapshot["rating"].toString()
                }
                withContext(Dispatchers.Main) {
                    userRating = ratingString
                    boardGame.userRating = ratingString
                    Log.v("get userRating", "${ratingString} + ${ratingSnapshot["rating"]}")
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
                    favoriteBoardGameList = favoriteBoardGameList.filter { it!!.id != id }
                    Log.v("remove fav list", "${favoriteBoardGameList.map { it?.id }}")
                }
            } catch (e: Exception) {
                Log.v("FirebaseTest", "Error writing document", e)
            }
        }
    }

    fun fetchFavoriteListFromDB() {
        val tempBg: ArrayList<BoardGame> = ArrayList()
        isLoading = true
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
            } catch (e: Exception) {
                favoriteBoardGameList = emptyList()
            } finally {
                isLoading = false
                favoriteBoardGameList = tempBg
            }
        }
    }


    fun fetchBoardGameData(id: String) {
        isLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val boardGame: BoardGame = repository.getBoardGame(id)
                Log.v("bgload", "bgnotloading: $boardGame")
                if (favoriteBoardGameList.any { it?.id == boardGame.id }) {
                    boardGame.isfavorite = true
                }
                fetchAverageRating(boardGame)
                fetchUserRating(boardGame)
                withContext(Dispatchers.Main) {
                    boardGameData = boardGame
                }
            } catch (e: Exception) {
                boardGameData = null
            } finally {
                isLoading = false
            }
        }
    }

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
        userAuthenticated = true
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
    fun verifySignedIn(): Boolean {
        return userAuthenticated
    }
}




