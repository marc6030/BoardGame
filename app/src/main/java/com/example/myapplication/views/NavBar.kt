package com.example.myapplication.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

class NavBar : ComponentActivity() {
    @Composable
    fun BottomNavigationBar(navController: NavHostController, actName: String) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Black,
            elevation = 0.dp
        ) {
             BottomNavigationItem(
                selected = actName == "Home",
                onClick = {
                    navController.navigate("home")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = Color.White.copy(alpha = 0.7f))
                },
            )


            BottomNavigationItem(
                selected = actName == "Personal",
                onClick = {
                    navController.navigate("personal")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Person", tint = Color.White.copy(alpha = 0.7f))
                },
            )
        }
    }

}