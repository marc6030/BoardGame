package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeActivity(navController: NavHostController) {
    val navBar = NavBar()
    navBar.BottomNavigationBar(navController,"Home")
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("State1")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Her kan du udløse navigation til en anden destination ved hjælp af navController
                navController.navigate("boardgameinfo/2536")
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Skift til State 2")
        }
    }
}

/*
class HomeActivity(navController: NavHostController) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navBar = NavBar()
        setContent{
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("State1")
                // Greeting(str)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val intent = Intent(this@HomeActivity, BoardGameInfoActivity::class.java)
                        intent.putExtra("gameID", "2536")
                        startActivity(intent)
                        finish()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Skift til State 2")
                }
            }
            navBar.BottomNavigationBar(this@HomeActivity,"Home")
        }
    }
}
*/