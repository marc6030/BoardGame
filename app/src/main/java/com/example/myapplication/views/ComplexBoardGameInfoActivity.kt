// ComplexBoardGameInfoActivity.kt
package com.example.myapplication


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel


@Composable
fun ComplexBoardGameInfoActivity(
    navController: NavHostController,
    boardGameInfoActivity: BoardGameInfoActivity,
    ratingsViewModel: RatingsViewModel,
    favoriteViewModel: FavoriteViewModel,
    sharedViewModel: SharedViewModel,
    gameID: String
    ) {


    val colorMatrix = ColorMatrix().apply {
        setToScale(0.2f, 0.2f, 0.2f, 1f)
    }


    val boardGame = boardGameInfoActivity.boardGameData
    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()



    var selectedTabIndex by remember { mutableStateOf(0) }


    AsyncImage(
        model = boardGame.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .blur(30.dp)
            //.scale(if (sharedViewModel.firstAnimationComplexBoardInfo) 1.5f else 0.3f)
            .animateContentSize(),
        colorFilter = ColorFilter.colorMatrix(colorMatrix)
    )


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
                    .background(MaterialTheme.colorScheme.background)
                    .align(Alignment.CenterHorizontally)
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
                            boardGame
                        )

                        1 -> generalInfo(
                            boardGame
                        )

                        2 -> ratingTab(
                            boardGameInfoActivity, ratingsViewModel
                        )
                    }
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
                .background(MaterialTheme.colorScheme.background),

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
        modifier = Modifier.background(Color.Black.copy(alpha = 0.2f))
    ) {
        texts.forEachIndexed { index, item ->
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
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
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White
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
                fontSize = 20.sp,
                color = Color.White
            )
            if (info2 != null) {
                Text(
                    text = info1 + " - " + info2,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp,
                    color = Color.White
                )
            } else {
                Text(
                    text = info1,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp,
                    color = Color.White
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
                color = Color.White
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
                            modifier = Modifier.fillMaxWidth(0.5f),
                            color = Color.White
                        )
                    }
                }
                Column {
                    for (i in (halfway + 1).toInt() until infoList.size) {
                        Text(
                            text = infoList.get(i),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(1f),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}





