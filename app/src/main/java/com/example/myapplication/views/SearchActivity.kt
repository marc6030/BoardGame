package com.example.myapplication.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.modelviews.MyViewModel
import com.example.myapplication.views.NavBar

@Composable
fun SearchActivity(navController: NavHostController, myViewModel: MyViewModel) {
    var input by remember { mutableStateOf("") }
    val searchQuery = input

    // Triggered every time the input changes.
    LaunchedEffect(key1 = searchQuery) {
        myViewModel.fetchGameBoardSearch(searchQuery)
    }

    // Observing the search results
    val searchResults = myViewModel.boardGameSearchResults.observeAsState().value?.boardGameSearchItems ?: emptyList()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // List of search results
        LazyColumn {
            items(searchResults) { result ->
                Text(text = result.name, modifier = Modifier.padding(16.dp))
            }
        }

        // Ensuring NavBar is at the bottom
        Spacer(modifier = Modifier.weight(1f))
        NavBar().BottomNavigationBar(navController, "Search")
    }
}
