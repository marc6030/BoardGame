package com.example.myapplication

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.views.MenuScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


// This is primarily a view. We should probably seperate the logic from the rest
@Composable
fun HomeActivity(navController: NavHostController, viewModel: BoardDataViewModel) {
    boardgameSelections(navController, viewModel)
}

@Composable
fun boardgameSelections(
    navController: NavHostController,
    viewModel: BoardDataViewModel
) {
    /*
    LaunchedEffect(Unit) {
        if (viewModel.boardGamesRow1.size < 2){
            viewModel.fetchBoardGameCategories()
            viewModel.getAllCategories()
        }

    }

     */

    MenuScreen(navController = navController, informationtext = "Is an app developed in Kotlin for Android. Its a platform for " +
            "board game enthusiasts. It features functionalities for exploring " +
            "various board games, providing users with detailed information about " +
            "each game. Users can browse different categories of board games, view " +
            "specific details, and possibly interact with some aspects related to " +
            "board gaming. The app's design caters to those interested in discovering " +
            "and learning more about board games, enhancing their gaming experience " +
            "with accessible information and user-friendly navigation.", ourColumn = { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)

            ) {
                item {
                    BigPicture(viewModel, navController, innerPadding.calculateTopPadding())
                }
                item {
                    boardGameSelection("Fighting", viewModel, 1, navController)
                }
                item {
                    SwipeableHotnessRow(viewModel.boardGamesRow0, navController, viewModel = viewModel)
                }
                item {
                    RoundboardGameSelection("Categories", viewModel, navController)
                }
                item {
                    boardGameSelection("Economic", viewModel, 2, navController)
                }
                item {
                    boardGameSelection("Card game", viewModel, 3, navController)
                }
                item {
                    boardGameSelection("Fantasy", viewModel, 4, navController)
                }
                item {
                    boardGameSelection("Racing", viewModel, 5, navController)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeableHotnessRow(
    items: List<BoardGameItem>,
    navController: NavHostController,
    autoScrollDuration: Long = 3000L,
    viewModel: BoardDataViewModel) {

    if (items.isNotEmpty()) {
        val pagerState = rememberPagerState(
            initialPage = items.size / 2,
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
                        val nextPage = (currentPage + 1).mod(10)
                        animateScrollToPage(page = nextPage)
                        currentPageKey = nextPage
                    }
                }
            }
        }
        Column(
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .height(400.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 20.dp),
                pageSpacing = 8.dp,
            ) { page ->
                val item = items[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()// Set a custom width for each item
                        .shadow(8.dp, RoundedCornerShape(30.dp))
                        .clickable {
                            navController.navigate("boardgameinfo/${item.id}")
                        }
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
                ) {
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
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .shadow(8.dp, CircleShape)
                            .background(color)
                            .size(12.dp)
                    )
                }
            }
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

    Text(
        text = headline,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 10.dp, top = 7.dp),
        color = MaterialTheme.colorScheme.onBackground,
        style = TextStyle(
            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
        )
    )
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
                    .clickable {
                        navController.navigate("boardgameinfo/$gameID")
                    }
                    .shadow(8.dp, RoundedCornerShape(8.dp))
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

@Composable
fun RoundboardGameSelection(headline: String,
                       viewModel: BoardDataViewModel,
                       navController: NavHostController,
){
    val scrollState = rememberLazyListState()

    val currentRow = viewModel.categories

    Text(
        text = headline,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 10.dp, top = 7.dp),
        color = MaterialTheme.colorScheme.onBackground,
        style = TextStyle(
            shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 8f)
        )
    )
    LazyRow(
        modifier = Modifier,
        state = scrollState
    )
    {

        items(currentRow.categories) { categoryName ->
            Box(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .testTag("items_1234")
                    .padding(5.dp)
                    .clickable {
                        val encodedCategoryName = Uri.encode(categoryName)
                        navController.navigate("category/$encodedCategoryName")
                    }
                    .shadow(8.dp, CircleShape)
            )
            {
                AsyncImage(
                    model = R.drawable.banditlogo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("game_picture")
                        .blur(10.dp)
                )
                Text(categoryName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                    ),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun BigPicture(
    viewModel: BoardDataViewModel,
    navController: NavHostController,
    topBarPadding: Dp
){
    val item = viewModel.bigPictureGame
    Log.v("MyTest", "${viewModel.bigPictureGame}")


    val gameName: String = item.name
    val gameID: String = item.id
    val gradientFrom = MaterialTheme.colorScheme.surface
    val gradientMidpoint = MaterialTheme.colorScheme.surfaceVariant
    val gradientTo = MaterialTheme.colorScheme.background
    Column(
        modifier = Modifier
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.1f to gradientFrom,
                            0.5f to gradientMidpoint,
                            1f to gradientTo),
                        tileMode = TileMode.Decal
                    )
                )
            }
    ){
        Spacer(modifier = Modifier.height(topBarPadding))
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(500.dp)
                .testTag("items_1234")
                .clickable {
                    navController.navigate("boardgameinfo/$gameID")
                }
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(10.dp)
                )
        )
        {
            AsyncImage(
                model = item.imageURL,
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

fun checkListsAreEmpty(viewModel: BoardDataViewModel): Boolean{
    return viewModel.boardGamesRow0.isEmpty()
            ||  viewModel.boardGamesRow1.isEmpty()
            || viewModel.boardGamesRow2.isEmpty()
            || viewModel.boardGamesRow3.isEmpty()
            || viewModel.boardGamesRow4.isEmpty()
            || viewModel.boardGamesRow5.isEmpty()
}
