// BoardGameInfoActivity.kt
package com.example.myapplication


import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.MyViewModel
import okio.blackholeSink


@Composable
fun BoardGameInfoActivity(
    navController: NavHostController,
    gameID: String?,
    viewModel: MyViewModel
) {
    val context = LocalContext.current

    // Use LaunchedEffect peoples! Is much importante!
    LaunchedEffect(gameID) {
        viewModel.fetchBoardGameData(gameID!!)
        // viewModel.isBoardGameFavourite(gameID)
        Log.v("Fetch Game ID in boardgamedata", "$gameID")
    }

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val boardGame by viewModel.boardGameData.observeAsState()
    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()

    if (gameID != null) {
        // Check internet Connection
        // Emil comment - maybe a bit much checking it everytime we load an item? - also we don't handle retries anyways?
        if (!isInternetAvailable(context)) {
            Text("No Internet!")
        }
        if (isLoading) {
            // Indikator
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
            var selectedTabIndex by remember {
                mutableStateOf(0)
            }
            // Observe the data
            if (boardGame != null) {
                // Data is available, update the UI
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = boardGame!!.name,
                        style = TextStyle(
                            fontSize = 50.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )

                    pictureAndKeyInfo(boardGame!!)

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                    ) {
                        Column() {
                            tabView(
                                texts = listOf(
                                    "Description",
                                    "General Info",
                                    "BoardBandit Rating"
                                )
                            ) {
                                selectedTabIndex = it;
                            }
                            when (selectedTabIndex) {
                                0 -> description(
                                    boardGame!!
                                )

                                1 -> generalInfo(
                                    boardGame!!
                                )
                                                         }
                        }
                    }
                }
                favoriteButton(navController, viewModel, boardGame!!)
            }
        }
    }
}


@Composable
fun pictureAndKeyInfo(boardGame: BoardGame){
    Row(
        modifier = Modifier.height(175.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = boardGame.imageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )

        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Image(
                        painter = painterResource(id = R.drawable.people_alt),
                        contentDescription = null,
                    )
                    Text(
                        text = "${boardGame.minPlayers} - ${boardGame.maxPlayers}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.av_timer),
                        contentDescription = null
                    )
                    Text(
                        text = "${boardGame.playingTime} min.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(1f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.elderly),
                        contentDescription = null
                    )
                    Text(
                        text = "${boardGame.age}+",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.fitness_center),
                        contentDescription = null
                    )
                    Text(
                        text = boardGame.averageWeight,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}

@Composable
fun tabView(
    modifier: Modifier = Modifier,
    texts: List<String>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val inactiveColor = Color(0xFF777777)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
    ) {
        texts.forEachIndexed { index, item ->
            Tab(
                modifier = modifier.background(Color.LightGray),
                selected = selectedTabIndex == index,
                selectedContentColor = Color.Black,
                unselectedContentColor = inactiveColor,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun description(boardGame: BoardGame) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            Box {
                Text(
                    text = boardGame.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun generalInfo(boardGame: BoardGame) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            simpleInfo("Player Count", boardGame.minPlayers, boardGame.maxPlayers)
            simpleInfo("Weight", boardGame.averageWeight, null)
            simpleInfo("BGG Rank", boardGame.overallRank, null)
            simpleInfo("Time", boardGame.playingTime, null)
            simpleInfo("Age", boardGame.age+"+", null)
            simpleInfo("BGG Rating", boardGame.ratingBGG, null)
            if(boardGame.category != "") {
                simpleInfo(boardGame.category, info1 = boardGame.categoryRank, info2 = null)
            }
            complexInfo(title = "Mechanisms", infoList = boardGame.mechanisms)
            complexInfo(title = "Categories", infoList = boardGame.categories)
            complexInfo(title = "Publishers", infoList = boardGame.publishers)
            complexInfo(title = "Artists", infoList = boardGame.artists)
            complexInfo(title = "Designers", infoList = boardGame.designers)
            complexInfo(title = "Families", infoList = boardGame.families)
        }
    }
}

@Composable
fun simpleInfo(title: String, info1: String, info2: String?) {
    Box{
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                text = title + ": ",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(0.65f),
                fontSize = 20.sp
            )
            if (info2 != null) {
                Text(
                    text = info1 + " - " + info2,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = info1,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun complexInfo(title: String, infoList : List<String>) {
    var halfway = kotlin.math.floor((infoList.size / 2).toDouble())
    if(infoList.size % 2 == 0){
        halfway = halfway - 1
    }
    Box {
        Column {
            Text(
                text = title + ":",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(0.5f),
                fontSize = 20.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    for (i in 0..(halfway).toInt()) {
                        Text(
                            text = infoList.get(i),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                }
                Column {
                    for (i in (halfway + 1).toInt() until infoList.size) {
                        Text(
                            text = infoList.get(i),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ratingTab(boardGame: BoardGame){
    Column {
        starDisplay(boardGame.ratingBGG, "BGG rating")
        Log.v("BGG Rating", "${boardGame.ratingBGG}")
    }
}

@Composable
fun starDisplay(){

}

@Composable
fun favoriteButton(navController: NavHostController,
                   viewModel: MyViewModel,
                   boardGame: BoardGame){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(8.dp),

            ) {

        }
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = null,
            modifier = Modifier
                .padding(18.dp)
        )
        Icon(
            imageVector = if (boardGame.isfavorite) Icons.Outlined.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite Icon",
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(55.dp)
                .padding(8.dp)
                .clickable {
                    viewModel.toggleFavorite(boardGame)
                    Log.v("is still fav", "${boardGame.isfavorite}")
                }
        )
    }
}


