package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BoardGame
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RatingsViewModel(private var sharedViewModel: SharedViewModel) : ViewModel() {
    var userRating by mutableStateOf("")
    var averageRating by mutableStateOf<Int?>(null)

    private var db = FirebaseFirestore.getInstance()

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }

    fun fetchRatings(gameID: String) {
        fetchAverageRating(gameID)
        fetchUserRating(gameID)
    }

    fun fetchAverageRating(gameID: String) {
        var average = 0
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratingSnapshot = db.collection("Ratings")
                    .document(gameID)
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
                    //boardGame.averageRatingBB = average
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

    fun fetchUserRating(gameID: String){
        var ratingString: String = ""
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ratingSnapshot = db.collection("Ratings")
                    .document(gameID)
                    .collection("userRatings")
                    .document(getUserID()).get()
                    .await()
                if(ratingSnapshot.get("rating") != null) {
                    ratingString = ratingSnapshot["rating"].toString()
                }
                withContext(Dispatchers.Main) {
                    userRating = ratingString
                    // boardGame.userRating = ratingString
                    Log.v("get userRating", "${ratingString} + ${ratingSnapshot["rating"]}")
                }
            } catch (e: Exception) {
                Log.e("fetchAverageRating", "Error fetching average rating", e)
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

    fun toggleRatings(boardGame: BoardGame?, rating: String) {
        if (boardGame != null) {
            if (boardGame.userRating != rating) {
                insertAverageRating(boardGame.id, rating)
            }
            else {
                removeRatingFromDB(boardGame.id)
                boardGame.userRating = ""
            }
            fetchRatings(boardGame.id)
        }
    }
}
