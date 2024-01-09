package com.example.myapplication.modelviews

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    //var boardGameList by mutableStateOf<BoardGameItems?>(null)
    var userAuthenticated by mutableStateOf(false)
    var isLoading by mutableStateOf(false)




    fun getUserID(): String {
        val userID: String = "static_user"
        Log.v("UserID is: ", userID)
        return userID
    }

}