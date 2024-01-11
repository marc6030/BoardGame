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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.modelviews.BoardDataViewModel

// played games, liked games, something fun
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
fun PersonalActivity(navController: NavHostController, viewModel: BoardDataViewModel) {
    val navBar = NavBar()
    val logo: Painter = painterResource(id = R.drawable.newbanditlogo)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
                ),
                title = {
                    androidx.compose.material.Icon(modifier = Modifier
                        .size(80.dp)
                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                        , painter = logo, contentDescription = "Logo", tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground )
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Localized description",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {navController.navigate("search")}) {
                        Icon(imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
            ) {
                navBar.BottomNavigationBar(navController, "Home")
            }
        }
    ) { innerPadding ->
        val gradientFrom = MaterialTheme.colorScheme.onError
        val gradientTo = MaterialTheme.colorScheme.background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .drawBehind {
                    drawRect(
                        Brush.linearGradient(
                            start = Offset.Zero,
                            end = Offset(this.size.width, this.size.height),
                            colorStops = arrayOf(
                                0f to gradientFrom,
                                0.75f to gradientTo
                            ),
                            tileMode = TileMode.Repeated
                        )
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ){
                Spacer(
                    Modifier.height(20.dp)
                )
                Box(
                    modifier = Modifier
                        .size(175.dp)
                        .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = "?",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                KeyStats()
                Spacer(modifier = Modifier.height(10.dp))
                Menu(navController)
                Recents(viewModel = viewModel, 1, navController)
            }
        }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        navBar.BottomNavigationBar(navController, "personal")
    }
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
            keyStat()
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
            keyStat()
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
fun keyStat(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Image(painter = painterResource(id = R.drawable.flamestreak),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "7",
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Color.White
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                text = "-------",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
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
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
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
            boardGameSelection(headline = "Recents", viewModel =viewModel, row =row, navController =navController)
        }
    }
}


@Composable
fun boardGameSelection(headline: String,
                       viewModel: BoardDataViewModel,
                       row: Int,
                       navController: NavHostController,
){
    val scrollState = rememberLazyListState()

    // currentrow is currently just a random category, but should be recent visited games.
    // val currentRow = viewModel.boardGamesRowRecent
    val currentRow = viewModel.boardGamesRow1
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.fetchAdditionalBoardGameCategories(row)
        }
    }
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
