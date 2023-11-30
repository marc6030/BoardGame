package com.example.myapplication.views


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.modelviews.BoardSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun searchActivity(navController: NavHostController, myViewModel: BoardSearchViewModel) {
    var input by remember { mutableStateOf("") }
    var lastInput by remember { mutableStateOf(input) }
    val coroutineScope = rememberCoroutineScope()

    // Accessing the search results directly from the MutableState
    val searchResults = myViewModel.boardGameSearch?.boardGameSearchItems ?: emptyList()

    Scaffold(
        bottomBar = { NavBar().BottomNavigationBar(navController, "Search") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it) // Padding to account for the BottomNavigationBar
        ) {
            // Search Bar
            TextField(
                value = input,
                onValueChange = {
                    input = it
                    lastInput = it
                    coroutineScope.launch {
                        delay(100) // 300 ms delay
                        if (lastInput == input) {
                            myViewModel.fetchGameBoardSearch(input)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )


            // List of search results
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(searchResults) { result ->
                    Button(
                        onClick = { navController.navigate("boardgameinfo/${result.id}") },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = result.name, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}