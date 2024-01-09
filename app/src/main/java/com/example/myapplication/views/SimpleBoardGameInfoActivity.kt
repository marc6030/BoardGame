package com.example.myapplication


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleBoardGameInfoActivity(navController: NavHostController,
                                ratingsViewModel: RatingsViewModel,
                                boardGameInfoActivity: BoardGameInfoActivity,
                                gameID: String
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }


    LaunchedEffect(Unit) {
        // Use LaunchedEffect peoples! Is much importante!
        boardGameInfoActivity.fetchBoardGameData(gameID)
        ratingsViewModel.fetchRatings(boardGameInfoActivity.currentGameID)
    }


    val colorMatrixDark = ColorMatrix().apply {
        setToScale(0.2f, 0.2f, 0.2f, 1f)
    }

    var boardGame =
        boardGameInfoActivity.boardGameData // It IS a var. It will not work as intended as a val. Trust me bro
    val textStyleBody1 = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp)
    var textStyle by remember { mutableStateOf(textStyleBody1) }
    var readyToDraw by remember { mutableStateOf(false) }


    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()
    DisposableEffect(boardGame!!.name) {
        textStyle = textStyleBody1
        readyToDraw = false
        onDispose { }
    }

    LaunchedEffect(boardGameInfoActivity.snackbarFavoriteVisible) {
        if (boardGameInfoActivity.snackbarFavoriteVisible) {
            delay(2000) // Delay for 2 seconds
            boardGameInfoActivity.snackbarFavoriteVisible = false
        }
    }

    AsyncImage(
        model = boardGame.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .blur(30.dp)
            .scale(1.5f)
            .animateContentSize(),
        colorFilter = ColorFilter.colorMatrix(colorMatrixDark)
    )
    VerticalPager(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = boardGameInfoActivity.openRatingPopUp || boardGameInfoActivity.openAddPopUp) {
                boardGameInfoActivity.openRatingPopUp = false
                boardGameInfoActivity.openAddPopUp = false
            },
        state = pagerState,
        pageContent = { page ->
            when (page) {
                0 -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(35.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Black)
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = boardGame!!.name,
                                    style = textStyle,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.2f)
                                        .padding(20.dp)
                                        .drawWithContent {
                                            if (readyToDraw) drawContent()
                                        },
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    overflow = TextOverflow.Clip,
                                    onTextLayout = { textLayoutResult ->
                                        if (textLayoutResult.didOverflowHeight) {
                                            textStyle =
                                                textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                                        } else {
                                            readyToDraw = true
                                        }
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight(0.67f)
                                        .padding(10.dp)
                                ) {
                                    AsyncImage(
                                        model = boardGame.imageURL,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxHeight(0.8f)
                                        .padding(10.dp)
                                        .fillMaxWidth(0.5f)
                                        .align(Alignment.CenterHorizontally)
                                        .background(Color.Transparent),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
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
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Text(
                                            text = "${boardGame.minPlayers} - ${boardGame.maxPlayers}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        Image(
                                            painter = painterResource(id = R.drawable.av_timer),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Text(
                                            text = "${boardGame.playingTime} min.",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
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
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Text(
                                            text = "${boardGame.age}+",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        Image(
                                            painter = painterResource(id = R.drawable.fitness_center),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color.White)
                                        )
                                        Text(
                                            text = boardGame.averageWeight,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                            }
                        }
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth(0.85f)
                                    .fillMaxHeight(0.863f)
                                    .align(Alignment.TopCenter)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "contentDescription",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .background(Color.DarkGray, CircleShape)
                                        .align(Alignment.BottomStart)
                                        .clickable {
                                            boardGameInfoActivity.openRatingPopUp =
                                                !boardGameInfoActivity.openRatingPopUp
                                        },
                                    tint = Color.DarkGray
                                )
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "contentDescription",
                                    modifier = Modifier
                                        .size(45.dp)
                                        .background(Color.DarkGray, CircleShape)
                                        .align(Alignment.BottomEnd)
                                        .clickable {
                                            boardGameInfoActivity.openAddPopUp =
                                                !boardGameInfoActivity.openAddPopUp
                                        },
                                    tint = Color.White,
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.845f)
                                    .align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = boardGame.ratingBGG,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .fillMaxWidth(0.292f)
                                        .align(Alignment.Bottom),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth(0.798f)
                                    .fillMaxHeight(0.845f)
                                    .align(Alignment.TopCenter)
                            ) {
                            }
                            Box(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth(1f)
                                    .fillMaxHeight(0.963f)
                                    .align(Alignment.TopCenter)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "contentDescription",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.DarkGray, CircleShape)
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(1)
                                            }
                                        },
                                    tint = Color.White,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth(0.693f)
                                    .fillMaxHeight(0.823f)
                                    .align(Alignment.TopCenter)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "contentdescription",
                                    modifier = Modifier
                                        .size(25.dp)
                                        .align(Alignment.BottomStart)
                                        .background(Color.Gray, CircleShape)
                                        .clickable {
                                            boardGameInfoActivity.openRatingPopUp =
                                                !boardGameInfoActivity.openRatingPopUp
                                        },
                                    tint = Color.White
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = boardGameInfoActivity.openRatingPopUp,
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        ) {
                            PopupRatingDialog(
                                boardGame = boardGame,
                                viewModel = ratingsViewModel
                            )
                        }
                        AnimatedVisibility(
                            visible = boardGameInfoActivity.openAddPopUp,
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        ) {
                            PopupAddDialog()
                        }
                        favoriteButton(boardGameInfoActivity = boardGameInfoActivity)
                    }
                }

                1 -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.background)
                                    .align(Alignment.CenterHorizontally),
                            ) {
                                Column() {
                                    tabView(
                                        texts = listOf(
                                            "Description",
                                            "General Info"
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
                                    }
                                }
                            }
                        }
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(30.dp)
                                    .fillMaxWidth(1f)
                                    .fillMaxHeight(0.225f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = "contentDescription",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.DarkGray, CircleShape)
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(0)
                                            }
                                        },
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
    Button(
        onClick = {
            navController.popBackStack()
            /*coroutineScope.launch {
                textStyle.copy(fontSize = 50.sp)
            }*/
        },
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
    }
    Image(
        painter = painterResource(id = R.drawable.ic_action_back),
        contentDescription = null,
        modifier = Modifier
            .padding(18.dp)
    )

}

@Composable
fun PopupRatingDialog(boardGame: BoardGame, viewModel: RatingsViewModel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                    Popup(
                        alignment = Alignment.Center,
                        properties = PopupProperties()
                    ) {
                        Box(
                            Modifier
                                .size(350.dp, 220.dp)
                                .padding(top = 5.dp)
                                .background(Color.DarkGray, RoundedCornerShape(10.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ratingTab(boardGame = boardGame, viewModel = viewModel)
                            }
                        }

                }
            }

    }
}

@Composable
fun PopupAddDialog() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties()
            ) {
                Box(
                    Modifier
                        .size(450.dp, 300.dp)
                        .padding(top = 5.dp)
                        .background(Color.DarkGray, RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                    }
                }
            }
        }
    }
}