package com.example.myapplication

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.NavBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue



// This is primarily a view. We should probably seperate the logic from the rest
@Composable
fun HomeActivity(navController: NavHostController, viewModel: BoardDataViewModel, favoriteViewModel: FavoriteViewModel, sharedViewModel: SharedViewModel) {

    val context = LocalContext.current
    // Check internet Connection - this does not belong here.
    if (!isInternetAvailable(context)) {
        Text("No Internet!")
    }
    LaunchedEffect(Unit) {
        viewModel.fetchBoardGameCategories()
        favoriteViewModel.fetchFavoriteListFromDB()
        delay(300)
        //sharedViewModel.animationHome = true
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

        boardgameSelections(navController, viewModel)

    }

}

@Composable
fun boardgameSelections(
    navController: NavHostController,
    viewModel: BoardDataViewModel
) {
    val navBar = NavBar()


    Column(
        modifier = Modifier
            .fillMaxSize()
        ){
        LazyColumn( modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Color.White))
        {
            item {
                TopMenu()
                SwipeableHotnessRow(viewModel.boardGamesRow0, navController)
                boardGameSelection("test", viewModel, 1, navController)
                boardGameSelection("Superhot", viewModel, 2, navController)
                boardGameSelection("rpggames", viewModel, 3, navController)
                boardGameSelection("dungeon games", viewModel, 4, navController)
                boardGameSelection("shooters", viewModel, 5, navController)
            }
        }
        navBar.BottomNavigationBar(navController, "Home")
    }
}

@Composable
fun TopMenu(){
    val logo: Painter = painterResource(id = R.drawable.newbanditlogo)
    val icon: Painter = painterResource(id = R.drawable.search)



    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Image(
            painter = logo,
            contentDescription = null, // Set a meaningful content description if needed
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableHotnessRow(
    items: List<BoardGameItem>,
    navController: NavHostController,
    autoScrollDuration: Long = 3000L
) {
    val pagerState = rememberPagerState(
        initialPage = items.size/2,
        initialPageOffsetFraction = 0f
    ) {
        items.size
    }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.height(400.dp),
        contentPadding = PaddingValues(horizontal = 32.dp),
        pageSpacing = 8.dp,
    ) { page ->
        val item = items[page]
        Box(
            modifier = Modifier
                .fillMaxSize()// Set a custom width for each item
                .clip(RoundedCornerShape(30.dp))
                .clickable { navController.navigate("boardgameinfo/${item.id}") }
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    val transformation =
                        lerp(
                            start = 0.7f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    alpha = transformation
                    scaleY = transformation
                }
        )  {
            AsyncImage(
                model = item.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                //alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
            )
        }
    }

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 10.dp, top = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(12.dp)
            )
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

    val currentRow = when (row) {
        1 -> viewModel.boardGamesRow1
        2 -> viewModel.boardGamesRow2
        3 -> viewModel.boardGamesRow3
        4 -> viewModel.boardGamesRow4
        5 -> viewModel.boardGamesRow5
        else -> emptyList() // Handle default case or invalid row
    }

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

    Text(text = headline, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 10.dp, top = 7.dp), color = Color.Black)
    LazyRow(
        modifier = Modifier,
        state = scrollState

    )
    {

        items(currentRow) { item ->
            val gameName: String = item.name
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


