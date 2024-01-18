package com.example.myapplication.modelviews

import BoardGameRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var userAuthenticated by mutableStateOf(false)

    fun getUserID(): String {
        val userID: String = "static_user"
        Log.v("UserID is: ", userID)
        return userID
    }

    fun checkOrCreateUser(): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BoardGameRepository().checkOrCreateUser(getUserID())
            } catch (e: Exception) {
                Log.v("Fetch Rated failed!: ", "$e")
            }
        }
    }

}