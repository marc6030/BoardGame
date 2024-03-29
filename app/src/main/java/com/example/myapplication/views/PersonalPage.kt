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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
    LaunchedEffect(Unit) {
        viewModel.fetchRecentBoardGames()
        viewModel.fetchKeyStats()
    }

    val profilepicture: Painter = painterResource(id = R.drawable.profilepicture)
    val bronze: Painter = painterResource(id = R.drawable.bronze)
    val silver: Painter = painterResource(id = R.drawable.silver)
    val gold: Painter = painterResource(id = R.drawable.gold)
    val plat: Painter = painterResource(id = R.drawable.plat)
    var showDialog by remember { mutableStateOf(false) }

    rankDecider(viewModel = viewModel)

    if (showDialog) {
        RankInfo(
            showDialog = showDialog,
            onDismissRequest = { showDialog = false },
        )
    }

    MenuScreen(navController = navController, informationtext = "Welcome to Your Personal Page!\n" +
            "\n" +
            "Your Personal Page is your own private space in our app where you can keep track of your progress. Here's what you can do here:" +
            "\n\n1. View Your Activity: All your recent visited board games" +
            "are displayed here.\n\n 2. Track Your Progress: Our app involves challenges, which can be completed by rating, playing, adding games to My Games and loggin into the app every day!" +
            "Your Personal Page will show your progress. Celebrate your achievements and plan your next steps, all " +
            " from this convenient dashboard.\n\n" +
            "You can also go up in rank, click the profile picture to find out how!",
        ourColumn = { innerPadding ->
            val gradientFrom = MaterialTheme.colorScheme.surface
            val gradientTo = MaterialTheme.colorScheme.background
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(
                            Brush.radialGradient(
                                center = Offset(this.size.width / 2, 525f),
                                radius = this.size.width * 1.5f,
                                colorStops = arrayOf(
                                    0f to gradientFrom,
                                    0.8f to gradientTo
                                ),
                                tileMode = TileMode.Decal
                            )
                        )
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Box(modifier = Modifier
                        //.size(300.dp) bronze
                        .size(250.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable { showDialog = true }
                    ) {
                        Box(
                            modifier = if (viewModel.platRank) Modifier
                                .size(115.dp)
                                .align(Alignment.Center) else Modifier
                                .size(140.dp)
                                .align(Alignment.Center)
                        ) {
                            Image(
                                contentDescription = "profile",
                                painter = profilepicture,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .fillMaxSize()
                            )
                        }
                        if (viewModel.bronzeRank && !viewModel.silverRank && !viewModel.goldRank && !viewModel.platRank) {
                            Image(
                                contentDescription = "bronze",
                                painter = bronze,
                                modifier = Modifier
                                    .fillMaxSize(0.98f)
                                    .align(Alignment.Center)
                                    .padding(bottom = 1.dp)
                            )
                        }
                        if (viewModel.silverRank && !viewModel.goldRank && !viewModel.platRank) {
                            Image(
                                contentDescription = "silver",
                                painter = silver,
                                modifier = Modifier
                                    .fillMaxSize(0.85f)
                                    .align(Alignment.Center)
                                    .padding(bottom = 2.dp)
                            )
                        }
                        if (viewModel.goldRank && !viewModel.platRank) {
                            Image(
                                contentDescription = "gold",
                                painter = gold,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                                    .padding(end = 15.dp)
                            )
                        }
                    }
                    KeyStats(viewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    Menu(navController)
                    Recents(viewModel = viewModel, navController)
                }
            }
            if (viewModel.platRank) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Box(modifier = Modifier
                        //.size(300.dp) bronze
                        .fillMaxHeight(0.33f)
                        .align(Alignment.CenterHorizontally)
                        .clickable { showDialog = true }
                    ) {
                        Image(
                            contentDescription = "plat",
                            painter = plat,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun KeyStats(viewModel: BoardDataViewModel){
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            showDialog = showDialog,
            onDismissRequest = { showDialog = false },
            text = "A Daily Logon Streak is a fun and rewarding feature we've introduced to enhance your experience with our app! Here's how it works:\n" +
                    "\n" +
                    "1. Log In Daily: Each day you log into our app, your streak goes up by one. It's that simple!\n" +
                    "\n" +
                    "2. Keep the Streak Alive: Make sure to log in every day to keep your streak going. If you miss a day, the streak resets to zero, so try to log in regularly."
        )
    }

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
            RatedStat(viewModel.nrOfRatedGames)
        }
        Box(
            modifier = Modifier
                .weight(0.33f)
                .height(70.dp)
                .clickable { showDialog = true }
        ) {
            StreakStat(viewModel.streak)
        }
        Box(
            modifier = Modifier
                .weight(0.33f)
                .height(70.dp)
        ) {
            playedGamesStat(viewModel.nrOfPlayedGames)
        }
    }
}

@Composable
fun StreakStat(streak : String){
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
                    text = if(streak.length == 0) "0" else streak,
                    modifier = if(streak.length < 2) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                    else if(streak.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 1.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 4.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
                    ),
                    fontSize = if(streak.length==1 ||streak.length == 0) 30.sp
                    else if(streak.length == 2) 25.sp
                    else if(streak.length == 3) 19.sp
                    else 14.sp)
            }
        }
        Text(text = "Streak",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
            ),
        )
    }
}
@Composable
fun RatedStat(nrOfRatedGames : String){
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
                    text = nrOfRatedGames,
                    modifier = if(nrOfRatedGames.length < 2) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 3.dp)
                    else if(nrOfRatedGames.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 5.dp)
                    else if(nrOfRatedGames.length < 4) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 12.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black, blurRadius = 8f)
                    ),
                    fontSize = if(nrOfRatedGames.length==1 ||nrOfRatedGames.length == 0) 29.sp
                    else if(nrOfRatedGames.length == 2) 24.sp
                    else if(nrOfRatedGames.length == 3) 19.sp
                    else 14.sp)
            }
        }
        Text(text = "Rated Games",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
            ),
        )
    }
}

@Composable
fun playedGamesStat(nrOfPlayedGames : String){
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
                    text = nrOfPlayedGames,
                    modifier = if(nrOfPlayedGames.length < 2) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                        else if(nrOfPlayedGames.length < 3) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 10.dp)
                    else if(nrOfPlayedGames.length < 4) Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 15.dp)
                    else Modifier
                        .fillMaxWidth(0.3f)
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 0.dp, 0.dp, 20.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black, blurRadius = 8f)
                    ),
                    fontSize = if(nrOfPlayedGames.length==1 || nrOfPlayedGames.length == 0) 25.sp
                    else if(nrOfPlayedGames.length == 2) 22.sp
                    else if(nrOfPlayedGames.length == 3) 15.sp
                    else 10.sp)
            }
        }
        Text(text = "Played Games",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(0.3f),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
            ),
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
                        .shadow(8.dp, RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable { navController.navigate("favorite") }
                ){
                    Text(
                        text = "My Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            navController.navigate("ratedGames")
                        }
                ){
                    Text(
                        text = "Rated Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                        )
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
                        .shadow(8.dp, RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            navController.navigate("playedGames")
                        }
                ){
                    Text(
                        text = "Played Games",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            navController.navigate("challenge")
                        }
                ){
                    Text(
                        text = "Challenges",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = TextStyle(
                            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Dialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    text: String
) {
    if (showDialog) {
        AlertDialog(
            icon = {
                androidx.compose.material.Icon(Icons.Filled.Info, contentDescription = "Info Icon", tint = MaterialTheme.colorScheme.onBackground)
            },
            title = {
                Text(text = "BoardGame Bandits", color = MaterialTheme.colorScheme.onBackground)
            },
            text = {
                Text(text = "$text", color = MaterialTheme.colorScheme.onBackground)
            },
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Close", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}
@Composable
fun Recents(viewModel: BoardDataViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .shadow(8.dp, RoundedCornerShape(5.dp))
                .background(Color.DarkGray)

        ) {
            recentBoardGameSelection(headline = "Recents", viewModel = viewModel, navController = navController)
        }
    }
}


@Composable
fun recentBoardGameSelection(headline: String,
                             viewModel: BoardDataViewModel,
                             navController: NavHostController,
){
    val scrollState = rememberLazyListState()

    val currentRow = viewModel.boardGamesRowRecent

    Column {
        androidx.compose.material3.Text(
            text = headline,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 10.dp, top = 7.dp),
            color = MaterialTheme.colorScheme.onBackground,
            style = TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
            )
        )
        LazyRow(
            modifier = Modifier.fillMaxHeight(),
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
                        .shadow(8.dp, RoundedCornerShape(5.dp))
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


fun rankDecider(viewModel: BoardDataViewModel){
    viewModel.bronzeRank = false
    viewModel.silverRank = false
    viewModel.goldRank = false
    viewModel.platRank = false

    if(viewModel.nrOfRatedGames.toInt() > 9
        || viewModel.nrOfPlayedGames.toInt() > 9
        || viewModel.nrOfLikedGames.toInt() > 9
        || viewModel.streak.toInt() >9){
        viewModel.bronzeRank = true
        if(viewModel.nrOfRatedGames.toInt() > 19 && viewModel.nrOfPlayedGames.toInt() > 19
            || viewModel.nrOfPlayedGames.toInt() > 19 && viewModel.nrOfLikedGames.toInt() > 19
            || viewModel.nrOfLikedGames.toInt() > 19 && viewModel.nrOfRatedGames.toInt() > 19
            || viewModel.streak.toInt() >19 && viewModel.nrOfRatedGames.toInt() > 19
            || viewModel.streak.toInt() >19 && viewModel.nrOfPlayedGames.toInt() > 19
            || viewModel.streak.toInt() >19 && viewModel.nrOfLikedGames.toInt() > 19
            ){
            viewModel.silverRank = true
            if(viewModel.nrOfRatedGames.toInt() > 29 && viewModel.nrOfPlayedGames.toInt() > 29 && viewModel.nrOfLikedGames.toInt() > 29
                || viewModel.nrOfRatedGames.toInt() > 29 && viewModel.nrOfPlayedGames.toInt() > 29 && viewModel.streak.toInt() >29
                || viewModel.nrOfRatedGames.toInt() > 29 && viewModel.nrOfLikedGames.toInt() > 29 &&  viewModel.streak.toInt() >29
                || viewModel.streak.toInt() >29 && viewModel.nrOfPlayedGames.toInt() > 29 && viewModel.nrOfLikedGames.toInt() > 29){
                viewModel.goldRank = true
                if(viewModel.nrOfRatedGames.toInt() > 29 && viewModel.nrOfPlayedGames.toInt() > 29
                    && viewModel.nrOfLikedGames.toInt() > 29 && viewModel.streak.toInt() >29){
                    viewModel.platRank = true
                }
            }
        }
    }
}

@Composable
fun RankInfo(
    showDialog: Boolean,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        AlertDialog(icon = {
                androidx.compose.material.Icon(
                    Icons.Filled.Info,
                    contentDescription = "Info Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            title = {
                Text(text = "Ranking System", color = MaterialTheme.colorScheme.onBackground)
            },
            text = {
                Text(text = "You can obtain 5 different ranks: Unranked Bronze, Silver, Gold and Platinum\n\n" +
                    "To obtain the Bronze rank you need to complete 1 Bronze challenge.\n\n" +
                    "To obtain the Silver rank you need to complete 2 Silver challenges.\n\n" +
                    "To obtain the Gold rank you need to complete 3 Gold challenges.\n\n" +
                    "To obtain the Platinum rank you need to complete all challenges.\n\n" +
                    "See the different challenges under the 'Challenges' page", color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
            },
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Close", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}