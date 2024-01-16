package com.example.myapplication.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.modelviews.SharedViewModel

@Composable
fun LoginScreen(viewmodel: SharedViewModel, navController: NavController, onSignInClick: () -> Unit) {

    val firebaseUser = viewmodel.userAuthenticated

    if (firebaseUser) { // Den skal ændre tilbage til '!=' for at den ikke springer login over
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
                Button(onClick = onSignInClick, colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.background
                )
                ) {
                    Text(text = "Sign In with Google")
                }
            }
        }
    }
}