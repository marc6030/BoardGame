package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.BoardGame
import com.example.myapplication.BoardGameItems
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SharedViewModel : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var userAuthenticated by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    var boardGameData by mutableStateOf<BoardGame?>(null)
    var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var favoriteBoardGameList by mutableStateOf<List<BoardGame?>>(emptyList())

    private lateinit var firebaseuser: FirebaseUser

    // Exposing the values for the views
    lateinit var db: FirebaseFirestore

    fun setDB(fbinstance: FirebaseFirestore) {
        this.db = fbinstance
    }

    // Updates the FirebaseUser and the user authentication status
    fun setUser(firebaseUser: FirebaseUser) {
        this.firebaseuser = firebaseUser
        userAuthenticated = true
    }


    fun getUserID(): String {
        val userID: String = this.firebaseuser.uid
        Log.v("UserID is: ", userID)
        return userID
    }

}