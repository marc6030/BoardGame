package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun SearchActivity(navController: NavHostController) {
    val navBar = NavBar()
    navBar.BottomNavigationBar(navController,"Search")
    Text(text = "Search!")
}