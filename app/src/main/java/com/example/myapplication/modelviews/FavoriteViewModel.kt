package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.example.myapplication.repositories.postgresql
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FavoriteViewModel(private var sharedViewModel: SharedViewModel, private var boardGameInfoActivity: BoardGameInfoActivity) : ViewModel() {


    private var db = FirebaseFirestore.getInstance()
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())


    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }

    fun setIsLoading(setme : Boolean) {
        sharedViewModel.isLoading = setme
    }


    fun fetchFavoriteListFromDB() {
        val tempBg: ArrayList<BoardGame> = ArrayList()
        // setIsLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val favSnapshot = db.collection("BBUsers").document(getUserID())
                    .collection("favorites")
                    .get()
                    .await()

                for (document in favSnapshot) {
                    val boardGame: BoardGame = postgresql().getBoardGame(document.id)
                    boardGame.isfavorite = true
                    tempBg.add(boardGame)
                }
            } catch (e: Exception) {
                favoriteBoardGameList = emptyList()
            } finally {
                setIsLoading(false)
                favoriteBoardGameList = tempBg
            }
        }
    }


    fun toggleFavorite(boardGame: BoardGame?): BoardGame {
        if (boardGame != null) {
            val updatedBoardGame = boardGame.copy(isfavorite = !boardGame.isfavorite)
            viewModelScope.launch(Dispatchers.Main) {
                if (updatedBoardGame.isfavorite) {
                    insertIntoUserFavoriteDB(boardGame.id)
                    favoriteBoardGameList = favoriteBoardGameList + updatedBoardGame
                } else {
                    removeFromUserFavoriteDB(boardGame.id)
                    favoriteBoardGameList = favoriteBoardGameList.filter { it?.id != boardGame.id }
                }
            }
            return updatedBoardGame
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
                val newFav = postgresql().getBoardGame(id)

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

}