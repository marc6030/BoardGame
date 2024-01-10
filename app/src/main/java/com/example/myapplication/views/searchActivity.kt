package com.example.myapplication.views


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.modelviews.BoardSearchViewModel
import kotlinx.coroutines.delay

@Composable
fun searchActivity(navController: NavHostController, myViewModel: BoardSearchViewModel) {
    val input = myViewModel.input
    val scrollState = rememberLazyListState()


    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 15
        }
    }

    LaunchedEffect(myViewModel.input){
        delay(300)
        myViewModel.fetchGameBoardSearch()
        scrollState.scrollToItem(0)
    }
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            myViewModel.fetchAdditionalSearchResults()
        }
    }

    Scaffold(
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
                    myViewModel.input = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            // List of search results
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                    state = scrollState
            ) {


                items(myViewModel.boardGameSearch) { result ->


                    Button(
                        onClick = { navController.navigate("boardgameinfo/${result.id}") },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = result.name, textAlign = TextAlign.Center)
                    }
                    // Trigger to load more items


                }
            }
        }
    }
}