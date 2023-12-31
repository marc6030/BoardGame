package com.example.myapplication.views

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

class NavBar : ComponentActivity() {
    @Composable
    fun BottomNavigationBar(navController: NavHostController, actName: String) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.primary,
        ) {
            BottomNavigationItem(
                selected = actName == "Home",
                onClick = {
                    navController.navigate("home")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
                },
                label = {
                    Text(text = "Home")
                }
            )

            BottomNavigationItem(
                selected = actName == "Search",
                onClick = {
                    navController.navigate("search")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                },
                label = {
                    Text(text = "Search")
                }
            )

            BottomNavigationItem(
                selected = actName == "Favorite",
                onClick = {
                    navController.navigate("favorite")
                },
                icon = {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite")
                },
                label = {
                    Text(text = "Favorite")
                }
            )
        }
    }

}