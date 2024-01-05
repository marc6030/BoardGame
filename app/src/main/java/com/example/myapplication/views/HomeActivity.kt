package com.example.myapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.imageManipulation.ImagedManipulation
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
    var startScaleInAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.fetchBoardGameList()
        favoriteViewModel.fetchFavoriteListFromDB()
        delay(300)
        startScaleInAnimation = true
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
        AnimatedVisibility(startScaleInAnimation,
            enter = expandVertically(),
            exit = fadeOut()){
            boardgameSelections(navController, sharedViewModel, viewModel)
        }
    }

}

@Composable
fun boardgameSelections(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    boardDataViewModel: BoardDataViewModel
) {
    val navBar = NavBar()
    val items = sharedViewModel.boardGameList
    val logo: Painter = painterResource(id = R.drawable.newbanditlogo)
    val icon: Painter = painterResource(id = R.drawable.search)
    if (items != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
            ){
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Image(
                    painter = logo,
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .align(Alignment.Center)
                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Image(
                    painter = icon,
                    contentDescription = null, // Set a meaningful content description if needed
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .align(Alignment.TopEnd)
                        .padding(0.dp, 10.dp, 0.dp, 10.dp)
                )

                }
            LazyColumn( modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Transparent))
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
            .background(Color.White)
            .padding(0.dp, 0.dp, 0.dp, 20.dp)
    ) { page ->
        val item = items[page]
        val item2 = items[page+1]
        Box(
            modifier = Modifier
                .fillMaxWidth() // Fill the max width of the pager
                .wrapContentWidth(Alignment.CenterHorizontally) // Center the box horizontally
                .clip(RoundedCornerShape(30.dp))
                .clickable { navController.navigate("boardgameinfo/${item.id}") }
        ) {
            AsyncImage(
                model = item.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center,
                modifier = Modifier.size(275.dp, 500.dp) // Size of the image
            )
        }
    }
}


@Composable
fun boardGameSelection(headline: String,
                       items: List<BoardGameItem>,
                       navController: NavHostController
){
    Text(text = headline, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 10.dp, top = 7.dp), color = Color.Black)
    LazyRow(
        modifier = Modifier
            .background(Color.White)
    )

    {
        items(items) { item ->
            val gameName: String = item.name
            val gameID: String = item.id
            val color = Color.LightGray
            //}
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
                    modifier = Modifier.fillMaxSize().testTag("game_picture")
                )
            }
        }
    }
}


