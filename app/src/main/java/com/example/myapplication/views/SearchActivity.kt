package com.example.myapplication.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
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
    val searchquery = input // bind input directly to search query

    // This will construct the URL every time the input changes.
    val url = "https://www.boardgamegeek.com/xmlapi2/hot?query=$searchquery"

    // Side effect for reacting to input changes.
    LaunchedEffect(key1 = searchquery) {
        // This is where you might call a ViewModel function to fetch the games using the updated URL.
        // For example:
        // myViewModel.fetchGames(url)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            EditSearchField(
                value = input,
                onValueChange = { input = it }, // Update input which will trigger LaunchedEffect
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                NavBar().BottomNavigationBar(navController, "Search")
            }
        }
    }
}

@Composable
fun EditSearchField(value: String,
                    onValueChange: (String) -> Unit,
                    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth()
    )
}
