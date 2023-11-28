package com.example.myapplication

import androidx.compose.foundation.Image
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.MyViewModel
import com.example.myapplication.views.NavBar


// This is primarily a view. We should probably seperate the logic from the rest
@Composable
fun HomeActivity(navController: NavHostController, viewModel: MyViewModel) {

    val context = LocalContext.current
    // Check internet Connection - this does not belong here.
    if (!isInternetAvailable(context)) {
        Text("No Internet!")
    }
    LaunchedEffect(Unit) {
        viewModel.fetchBoardGameList()
        viewModel.fetchFavoriteListFromDB()
    }

    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    if (isLoading) {
        Row(
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
        boardgameSelections(navController, viewModel)
    }

}

@Composable
fun boardgameSelections(
    navController: NavHostController,
    viewModel: MyViewModel
) {
    val navBar = NavBar()
    val items = viewModel.boardGameDataList.observeAsState().value
    val logo: Painter = painterResource(id = R.drawable.banditlogo)
    if (items != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            ){
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Image(
                        painter = logo,
                        contentDescription = null, // Set a meaningful content description if needed
                        modifier = Modifier
                            .height(120.dp)
                            .width(120.dp)
                            .align(Alignment.Center)
                    )
                }
                Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                        )
            LazyColumn( modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White))
            {
                item {
                    boardGameSelection("Hotness", items.boardGames, navController)
                    boardGameSelection("test", items.boardGames, navController)
                    boardGameSelection("Superhot", items.boardGames, navController)
                    boardGameSelection("rpggames", items.boardGames, navController)
                    boardGameSelection("dungeon games", items.boardGames, navController)
                    boardGameSelection("shooters", items.boardGames, navController)

                }
            }
            navBar.BottomNavigationBar(navController, "Home")
        }

    }
}

@Composable
fun boardGameSelection(headline: String,
                       items: List<BoardGameItem>,
                       navController: NavHostController
){
    Text(text = headline, fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp, top = 20.dp))
    LazyRow(
        modifier = Modifier
            .background(Color.White)
    )
    {
        items(items) { item ->
            val gameName: String = item.name
            val gameID: String = item.id
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .testTag("items_1234")
                    .padding(5.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.navigate("boardgameinfo/$gameID")
                    }
            )
            {
                AsyncImage(
                    model = item.imgUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().testTag("game_picture")
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(1f)
                        .fillMaxHeight(0.33f)
                        .background(
                            Color.LightGray,
                            shape = MaterialTheme.shapes.large
                        )
                ) {
                    Text(
                        text = item.name,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}


