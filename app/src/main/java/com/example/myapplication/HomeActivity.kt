package com.example.myapplication


import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

//@Composable
//fun HomeActivity(navController: NavHostController, viewModel: MyViewModel) {
//    val navBar = NavBar()
//    navBar.BottomNavigationBar(navController, "Home")
//
//    Row (
//            Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        )
//        {
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .size(200.dp),
//                strokeWidth = 50.dp
//            )
//        }
//
//}

@Composable
fun HomeActivity(navController: NavHostController, viewModel: MyViewModel) {
    val navBar = NavBar()
    val url = "https://boardgamegeek.com/xmlapi2/hot?boardgame"
    val context = LocalContext.current
    // Check internet Connection
    if (!isInternetAvailable(context)) {
        Text("No Internet!")
    }

    viewModel.fetchBoardGameList(url) // Henter dataen

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    if (isLoading) {
        // Indikator
        Row (
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(200.dp),
                strokeWidth = 50.dp
            )
        }
    } else {

        val items = viewModel.boardGameDataList.value
        if(items != null) {
            Column( modifier = Modifier
                .fillMaxSize()) {
                LazyRow (
                    modifier = Modifier
                        .weight(1f), // Takes up remaining available space
                )
                {
                    items(items.boardGames){item ->
                        Box(
                            modifier = Modifier
                                .padding(16.dp)

                        )
                        {
                            Text(text = item.name)
                        }

                    }
                }
                navBar.BottomNavigationBar(navController, "Home")
            }
        }
    }
}

//                    items(items.boardGames) { item ->
//                        val gameName: String = item.name
//                        val gameID: String = item.id
//                        Button(
//                            onClick = {
//                                navController.navigate("boardgameinfo/$gameID")
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                        ) {
//                            Text(gameName)
//                        }
//                    }
