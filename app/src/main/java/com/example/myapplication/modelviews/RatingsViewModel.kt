package com.example.myapplication.modelviews

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class RatingsViewModel(private var sharedViewModel: SharedViewModel) : ViewModel() {
    var userRating by mutableStateOf("")

    private var db = FirebaseFirestore.getInstance()

    private fun getUserID() : String {
        return sharedViewModel.getUserID()
    }



}
