package com.example.myapplication.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.modelviews.BoardDataViewModel

// played games, liked games, something fun
    @Composable
fun PersonalActivity(navController: NavHostController, viewModel: BoardDataViewModel) {
    viewModel.fetchRecentBoardGames()
    val navBar = NavBar()
    val logo: Painter = painterResource(id = R.drawable.newbanditlogo)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            androidx.compose.material.Icon(modifier = Modifier
                .size(75.dp)
                .padding(0.dp, 5.dp, 0.dp, 0.dp)
                .align(Alignment.TopCenter)
                , painter = logo, contentDescription = "Logo" )
            Icon(modifier = Modifier
                .padding(0.dp, 18.dp, 15.dp, 0.dp)
                .align(Alignment.TopEnd),
                imageVector = Icons.Filled.Search,
                contentDescription = "Localized description",
                tint = Color.Black
            )
            Icon(modifier = Modifier
                .padding(15.dp, 18.dp, 0.dp, 0.dp)
                .align(Alignment.TopStart),
                imageVector = Icons.Filled.Info,
                contentDescription = "Localized description",
                tint = Color.Black
            )
        }
        Spacer(
            Modifier.height(20.dp)
        )
        Box(
            modifier = Modifier
                .size(100.dp, 100.dp)
                .background(Color.DarkGray, shape = CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "?",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        KeyStats()
        Spacer(modifier = Modifier.height(10.dp))
        Menu(navController)
        Recents(viewModel = viewModel, 1, navController)
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        navBar.BottomNavigationBar(navController, "personal")
    }
}

@Composable
fun KeyStats(){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.33f)
                .height(70.dp)
        ) {
            RatedStat()
        }
        Box(
            modifier = Modifier
                .weight(0.33f)
                .height(70.dp)
        ) {
            StreakStat()
        }
        Box(
            modifier = Modifier
                .weight(0.33f)
                .height(70.dp)
        ) {
            playedGamesStat()
        }
    }
}

@Composable
fun StreakStat(){
    val string = "90"
    Column(modifier = Modifier.fillMaxSize())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
            Image(painter = painterResource(id = R.drawable.flamestreak),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center))
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomCenter)) {
                Text(
                    text = if(string.length == 0) "0" else string,
                    modifier = if(string.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 1.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 4.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = if(string.length==1 ||string.length == 0) 30.sp
                    else if(string.length == 2) 25.sp
                    else if(string.length == 3) 19.sp
                    else 14.sp)
            }
        }
        Text(text = "Streak",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}
@Composable
fun RatedStat(){
    val string = "80"
    Column(modifier = Modifier.fillMaxSize())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
            Image(painter = painterResource(id = R.drawable.starstat),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center))
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomCenter)) {
                Text(
                    text = if(string.length == 0) "0" else string,
                    modifier = if(string.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 5.dp)
                    else if(string.length < 4) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = if(string.length==1 ||string.length == 0) 29.sp
                    else if(string.length == 2) 24.sp
                    else if(string.length == 3) 19.sp
                    else 14.sp)
            }
        }
        Text(text = "Rated Games",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun playedGamesStat(){
    val string = "9"
    Column(modifier = Modifier.fillMaxSize())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
        ) {
            Icon(painter = painterResource(id = R.drawable.token),
                contentDescription = "token",
                modifier = Modifier
                    .align(Alignment.Center),
                tint = Color.Red)
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomCenter)) {
                Text(
                    text = if(string.length == 0) "0" else string,
                    modifier = if(string.length < 2) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                        else if(string.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    else if(string.length < 4) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 15.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 20.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if(string.length==1 ||string.length == 0) 25.sp
                    else if(string.length == 2) 22.sp
                    else if(string.length == 3) 15.sp
                    else 10.sp)
            }
        }
        Text(text = "Played Games",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun Menu(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .padding(end = 7.dp)
            ){
                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable { navController.navigate("favorite") }
                ){
                    Text(
                        text = "My Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable { //navcontroller.navigate("ratedGames")
                        }
                ){
                    Text(
                        text = "Rated Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 7.dp)
            ){

                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            //navcontroller.navigate("playedGames")
                        }
                ){
                    Text(
                        text = "Played Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            // navController.navigate("challenges")
                        }
                ){
                    Text(
                        text = "Challenges",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun Recents(viewModel: BoardDataViewModel, row: Int, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.DarkGray)

        ) {
            recentBoardGameSelection(headline = "Recents", viewModel =viewModel, navController =navController)
        }
    }
}


@Composable
fun recentBoardGameSelection(headline: String,
                             viewModel: BoardDataViewModel,
                             navController: NavHostController,
){
    val scrollState = rememberLazyListState()

    // currentrow is currently just a random category, but should be recent visited games.
    // val currentRow = viewModel.boardGamesRowRecent
    val currentRow = viewModel.boardGamesRowRecent

    Column {
        androidx.compose.material3.Text(
            text = headline,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 10.dp, top = 7.dp),
            color = Color.White
        )
        LazyRow(
            modifier = Modifier,
            state = scrollState

        )
        {

            items(currentRow) { item ->
                val gameID: String = item.id
                Box(
                    modifier = Modifier
                        .size(100.dp, 150.dp)
                        .testTag("items_1234")
                        .padding(5.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .clickable {
                            navController.navigate("boardgameinfo/$gameID")
                        }
                )
                {
                    AsyncImage(
                        model = item.imgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("game_picture")
                    )
                }
            }
        }
    }
}
