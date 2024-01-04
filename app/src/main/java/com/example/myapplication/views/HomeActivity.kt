package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.NavBar
import kotlinx.coroutines.delay


// This is primarily a view. We should probably seperate the logic from the rest
@Composable
fun HomeActivity(navController: NavHostController, viewModel: BoardDataViewModel, favoriteViewModel: FavoriteViewModel, sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    // Check internet Connection - this does not belong here.
    if (!isInternetAvailable(context)) {
        Text("No Internet!")
    }
    LaunchedEffect(Unit) {
        viewModel.fetchBoardGameList()
        favoriteViewModel.fetchFavoriteListFromDB()
        delay(300)
        sharedViewModel.animationHome = true
    }

    val isLoading = sharedViewModel.isLoading

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
        AnimatedVisibility(sharedViewModel.animationHome,
            enter = fadeIn(),
            exit = fadeOut()
        ){
            boardgameSelections(navController, sharedViewModel)
        }
    }

}

@Composable
fun boardgameSelections(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val navBar = NavBar()
    val items = sharedViewModel.boardGameList
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
                    SwipeableHotnessRow(items.boardGames, navController)
                    boardGameSelection("test", items.boardGames, navController)
                    boardGameSelection("Superhot", items.boardGames, navController)
                    boardGameSelection("rpggames", items.boardGames, navController)
                    boardGameSelection("dungeon games", items.boardGames, navController)
                    boardGameSelection("shooters", items.boardGames, navController)
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
        Box(contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
            ){
            Spacer(modifier = Modifier
                .fillMaxSize()
                .blur(10.dp))
        }
        Box(contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()) {
            navBar.BottomNavigationBar(navController, "Home")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableHotnessRow(
    items: List<BoardGameItem>,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        items.size
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) { page ->
        val item = items[page]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { navController.navigate("boardgameinfo/${item.id}") }
        ) {
            AsyncImage(
                model = item.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color(0x66000000), // Semi-transparent black
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun boardGameSelection(headline: String,
                       items: List<BoardGameItem>,
                       navController: NavHostController
) {
    Text(
        text = headline,
        fontSize = 35.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 10.dp, top = 20.dp)
    )
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
