package com.example.myapplication.views

import androidx.compose.foundation.layout.*

import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.modelviews.MyViewModel

@Composable
fun LoginScreen(viewmodel: MyViewModel, navController: NavController, onSignInClick: () -> Unit) {

    val firebaseUser by viewmodel.isUserLoggedInGoogle.observeAsState()

    if (firebaseUser == null) { // Den skal ændre tilbage til '!=' for at den ikke springer login over
        LaunchedEffect(firebaseUser) {
            navController.navigate("home")
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Welcome to Boardgame App")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSignInClick) {
                    Text(text = "Sign In with Google")
                }
            }
        }
    }
}