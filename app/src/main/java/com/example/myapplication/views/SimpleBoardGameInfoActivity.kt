package com.example.myapplication


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.views.YoutubePlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.random.Random

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
    var showYouTubePlayer by remember { mutableStateOf(false) }

    LaunchedEffect(gameID){
        boardGameInfoActivity.fetchBoardGameData(gameID)
        boardGameInfoActivity.addToRecentBoardGames(gameID)
        // boardGameInfoActivity.fetchAverageRating(gameID)
    }



    val colorMatrixDark = ColorMatrix().apply {
        setToScale(0.5f, 0.5f, 0.5f, 1f)
    }

    val boardGame =
        boardGameInfoActivity.boardGameData // It IS a var. It will not work as intended as a val. Trust me bro
    val textStyleBody1 = MaterialTheme.typography.headlineLarge.copy(
        textAlign = TextAlign.Center,
        fontSize = 50.sp,
        shadow = Shadow(color = Color.Black, blurRadius = 6f)
    )
    var textStyle by remember { mutableStateOf(textStyleBody1) }
    var readyToDraw by remember { mutableStateOf(false) }


    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()
    DisposableEffect(boardGame.name) {
        textStyle = textStyleBody1
        readyToDraw = false
        onDispose { }
    }

    AsyncImage(
        model = boardGame.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .blur(10.dp)
            .scale(1.3f)
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Transparent)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = boardGame.name,
                                    style = textStyle,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxHeight(0.2f)
                                        .fillMaxWidth()
                                        .padding(20.dp, 30.dp, 20.dp, 20.dp)
                                        .drawWithContent {
                                            if (readyToDraw) drawContent()
                                        },
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Clip,
                                    onTextLayout = { textLayoutResult ->
                                        if (textLayoutResult.didOverflowHeight) {
                                            textStyle =
                                                textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                                        } else {
                                            readyToDraw = true
                                        }
                                    },
                                    textAlign = TextAlign.Center,
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
                                            .shadow(8.dp, CircleShape)
                                    )
                                    IconButton(
                                        onClick = { showYouTubePlayer = true },
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(80.dp)
                                            .clip(CircleShape)
                                            .background(
                                                MaterialTheme.colorScheme.background.copy(
                                                    alpha = 0.5f
                                                )
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.PlayArrow,
                                            contentDescription = "Localized description",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .alpha(0.7f)
                                        )
                                    }
                                    if (showYouTubePlayer) {
                                        val temp = boardGame.name.replace(" ", "")
                                        Log.v("Boevs2", "${boardGame.name.replace(" ", "")}")
                                        boardGameInfoActivity.fetchYoutubeID(temp)
                                        YoutubePlayer(
                                            youtubeVideoId = boardGameInfoActivity.youtubeID,
                                            lifecycleOwner = LocalLifecycleOwner.current
                                        )

                                        // Close Button
                                        IconButton(
                                            onClick = { showYouTubePlayer = false },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(16.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Close",
                                                tint = Color.White
                                            )
                                        }
                                    }
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
                                            colorFilter = ColorFilter.tint(Color.White),
                                            modifier = Modifier.shakeAndOffsetClickable(
                                                onClick = { /* your click logic */ },
                                                offsetX = 700.dp
                                            )
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
                                        .background(
                                            MaterialTheme.colorScheme.background,
                                            CircleShape
                                        )
                                        .align(Alignment.BottomStart)
                                        .clickable {
                                            boardGameInfoActivity.openRatingPopUp =
                                                !boardGameInfoActivity.openRatingPopUp
                                        },
                                    tint = MaterialTheme.colorScheme.background
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
                                        .fillMaxWidth(0.280f)
                                        .fillMaxHeight(0.03f)
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
                                        .shadow(8.dp, CircleShape)
                                        .background(MaterialTheme.colorScheme.background)
                                        .align(Alignment.BottomCenter)
                                        .clickable {
                                            boardGameInfoActivity.openRatingPopUp = false
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(1)
                                            }
                                        },
                                    tint = MaterialTheme.colorScheme.onBackground,
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
                                        .background(Color.DarkGray, CircleShape)
                                        .clickable {
                                            boardGameInfoActivity.openRatingPopUp =
                                                !boardGameInfoActivity.openRatingPopUp
                                        },
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = boardGameInfoActivity.openRatingPopUp,
                            enter = slideInVertically(),
                            exit = ExitTransition.None
                        ) {
                            PopupRatingDialog(
                                boardGameInfoActivity = boardGameInfoActivity,
                                viewModel = ratingsViewModel
                            )
                        }
                        addPlayedGamesButton(boardGameInfoActivity = boardGameInfoActivity)
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
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .align(Alignment.CenterHorizontally),
                            ) {
                                Column(modifier = Modifier.background(Color.Transparent)) {
                                    tabView(
                                        texts = listOf(
                                            "Description",
                                            "General Info"
                                        ),
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
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                    }
                }
            }
        }
    )
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        IconButton(
            onClick = {
                boardGameInfoActivity.openRatingPopUp = false
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute != "home" && currentRoute != "personal") {
                    navController.popBackStack()}
                  },
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "back arrow",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun PopupRatingDialog(boardGameInfoActivity: BoardGameInfoActivity, viewModel: RatingsViewModel) {
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
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        ratingTab(boardGameInfoActivity = boardGameInfoActivity, viewModel = viewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun ratingTab(boardGameInfoActivity: BoardGameInfoActivity, viewModel: RatingsViewModel) {
    Column {
        starDisplay(boardGameInfoActivity.boardGameData.ratingBGG, "BGG rating")
        starDisplay(boardGameInfoActivity.averageRating.toString(),
            text = "BoardBandit Average Rating"
        )
        ratingDisplay(
            text = "Your Rating",
            viewModel = viewModel,
            boardGameInfoActivity = boardGameInfoActivity
        )
        Log.v("BGG Rating", "${boardGameInfoActivity.boardGameData.ratingBGG}")
    }
}

@Composable
fun starDisplay(stars: String, text: String) {
    val numOfStars: Double = stars.toBigDecimal().setScale(1, RoundingMode.CEILING).toDouble()
    Column {
        Box (modifier = Modifier.padding(2.dp)){
            Text(text + ": $numOfStars / 10", color = Color.White, style = TextStyle(
                shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
            ))
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentWidth(Alignment.Start)
        ) {
            Row() {
                for (i in 1..10) {
                    if (numOfStars >= i) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite Icon",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .size(34.dp)

                            // .border(BorderStroke(2.dp, color = Color.Black), 2.dp, Shape = ShapeTokens.BorderDefaultShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite Icon",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(34.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ratingDisplay(
                    text: String,
                    viewModel: RatingsViewModel,
                    boardGameInfoActivity: BoardGameInfoActivity
                ) {
    val numOfStars = boardGameInfoActivity.boardGameData.user_rating.toDouble()
    Log.v("current game: ", "${boardGameInfoActivity.boardGameData}")
    Log.v("current rating: ", boardGameInfoActivity.boardGameData.user_rating)


    Column {
        Box (modifier = Modifier.padding(2.dp)){
            Text(text + ": $numOfStars / 10 - Rate by tapping a Star", color = Color.White,
                style = TextStyle(
                    shadow = Shadow(color = Color.Black, offset = Offset(1f, 1f), blurRadius = 6f)
                ))
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentWidth(Alignment.Start)
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Row() {
                for (i in 1..10) {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Ratings Icon",
                        tint = if (numOfStars >= i) Color.White else Color.Black,
                        modifier = Modifier
                            .size(34.dp)
                            .clickable {
                                boardGameInfoActivity.updateRating(
                                    i.toString()
                                )
                            }
                    )
                }
            }
        }
    }
}


@Composable
fun favoriteButton(
                    boardGameInfoActivity: BoardGameInfoActivity,
                ) {
    var triggerConfetti by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.86f)
            .fillMaxHeight(0.73f)
    ) {
        Icon(imageVector = if (boardGameInfoActivity.boardGameData.liked == "True") Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "favoriteButton",
            tint = Color.White,
            modifier = Modifier
                .size(45.dp)
                .background(Color.Transparent, CircleShape)
                .align(Alignment.BottomEnd)
                .bounceClickable {
                    triggerConfetti = true
                    boardGameInfoActivity.toggleFavorite(boardGameInfoActivity.boardGameData.id)
                    boardGameInfoActivity.snackbarFavoriteVisible =
                        !boardGameInfoActivity.snackbarFavoriteVisible
                    Log.v(
                        "is still fav",
                        "${boardGameInfoActivity.boardGameData!!.isfavorite}"
                    )
                    if (boardGameInfoActivity.snackbarFavoriteVisible) {
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = if (boardGameInfoActivity.boardGameData.liked == "True") "Added to My Games" else "Removed from My Games",
                                actionLabel = "UNDO",
                                duration = SnackbarDuration.Short,
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                triggerConfetti = true
                                boardGameInfoActivity.toggleFavorite(boardGameInfoActivity.boardGameData.id)
                            }
                            boardGameInfoActivity.snackbarFavoriteVisible =
                                false
                        }
                    }
                }
        )

        DisposableEffect(triggerConfetti) {
            if (triggerConfetti && boardGameInfoActivity.boardGameData.liked == "True") {
                coroutineScope.launch {
                    delay(2000)
                    triggerConfetti = false
                }
            }
            onDispose {
            }
        }
        if (triggerConfetti && boardGameInfoActivity.boardGameData.liked == "True") {
            ParticleSystem(
                23.dp,
                20.dp,
                200,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(55.dp)
                    .padding(8.dp)
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (boardGameInfoActivity.snackbarFavoriteVisible) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = MaterialTheme.colorScheme.background, // Change the color of the "UNDO" text
                    containerColor = Color.Gray, // Change the background color of the Snackbar
                    contentColor = Color.White
                )// Change the color of the text in the Snackbar
            }
        }
    }
}


data class Particle(
    val position: Offset,
    val velocity: Offset,
    val acceleration: Offset,
    val color: Color,
    val size: Float,
    val isCircle: Boolean)

@Composable
fun ParticleSystem(
                    posXInDp: Dp,
                    posYInDp: Dp,
                    size: Int,
                    modifier: Modifier
                ) {

    val posX = with(LocalDensity.current) { posXInDp.toPx() }
    val posY = with(LocalDensity.current) { posYInDp.toPx() }

    // List of particles
    val particles: MutableList<Particle> = mutableListOf()
    for (i in 1..size) {
        val colors = listOf(
            Color(255, 0, 0, 255),
            Color(0, 255, 0, 255),
            Color(0, 0, 255, 255),
            Color(255, 152, 0, 255),
            Color(255, 235, 59, 255)
        )

        particles.add(
            Particle(
                Offset(posX, posY),
                Offset(1F, 1F),
                Offset(0F, 0F),
                colors[Random.nextInt(colors.size)],
                10f,
                Random.nextBoolean()
            )
        )
    }

    val mutableParticles = remember { mutableStateListOf<Particle>() }
    mutableParticles.addAll(particles)

    var counter = 0

    // Updates each particle pos, vel, acc, size and alpha
    LaunchedEffect(Unit) {
        while (true) {
            val particlesCopy =
                ArrayList(mutableParticles.map { it.copy() })
            particlesCopy.forEachIndexed { index, particle ->
                mutableParticles[index] =
                    particle.copy(
                        position = particle.position + particle.velocity,
                        velocity = particle.velocity + particle.acceleration + Offset(
                            0.0f,
                            0.3f
                        ),
                        acceleration = Offset(
                            (Math.random() * 2 - 1).toFloat(),
                            (Math.random() * 2 - 1).toFloat()
                        ),
                        color = particle.color.copy(
                            red = particle.color.red,
                            green = particle.color.green,
                            blue = particle.color.blue,
                            alpha = (particle.color.alpha + (Math.random() * 5 - 4).toFloat()).coerceIn(
                                0F,
                                1F
                            )
                        ),
                        size = (particle.size + (Math.random() * 2 - 1).toFloat()).coerceIn(
                            8.0f,
                            12.0f
                        )
                    )

            }

            delay(16L)
            counter += 1

            if (counter > 200) {
                mutableParticles.clear()
                break
            }
        }
    }

    // Draws the canvas with the particles on
    Canvas(
        modifier = modifier
    ) {
        mutableParticles.forEach { particle ->
            if (particle.isCircle) {
                drawCircle(
                    color = particle.color,
                    alpha = 0.6F,
                    center = particle.position,
                    radius = particle.size
                )
            } else {
                drawRect(
                    color = particle.color,
                    alpha = 0.6F,
                    topLeft = particle.position,
                    size = Size(
                        particle.size,
                        particle.size
                    )
                )
            }
        }
    }
}

@Composable
fun addPlayedGamesButton(boardGameInfoActivity: BoardGameInfoActivity) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.863f)
                .align(Alignment.TopCenter)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "contentDescription",
                modifier = Modifier
                    .size(45.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        boardGameInfoActivity.addOrRemovePlayedGames(
                            boardGameInfoActivity.currentGameID,
                            "True"
                        )
                        boardGameInfoActivity.snackbarChallengeVisible = true
                        if (boardGameInfoActivity.snackbarChallengeVisible) {
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Congratulation on playing this game! Added to Played Games",
                                    actionLabel = "UNDO",
                                    duration = SnackbarDuration.Short,
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    boardGameInfoActivity.addOrRemovePlayedGames(
                                        boardGameInfoActivity.currentGameID,
                                        "False"
                                    )
                                }
                                boardGameInfoActivity.snackbarChallengeVisible =
                                    false
                            }
                        }
                    },
                tint = Color.White,
            )
        }
        if (boardGameInfoActivity.snackbarChallengeVisible) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = Color.LightGray, // Change the color of the "UNDO" text
                    containerColor = Color.Gray, // Change the background color of the Snackbar
                    contentColor = Color.White
                )// Change the color of the text in the Snackbar
            }
        }
    }
}

fun Modifier.bounceClickable(
    minScale: Float = 0.5f,
    onAnimationFinished: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) minScale else 1f,
        label = ""
    ) {
        if(isPressed) {
            isPressed = false
            onAnimationFinished?.invoke()
        }
    }

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable {
            isPressed = true
            onClick?.invoke()
        }
}


@Composable
fun Modifier.shakeAndOffsetClickable(
    onClick: () -> Unit,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) = composed {
    var isPressed by remember { mutableStateOf(false) }

    val rotationOffset by animateFloatAsState(
        targetValue = if (isPressed) 10f else 0f,
        animationSpec = keyframes {
            durationMillis = 3000
            0.0f at 0
            15.0f at 150
            -15.0f at 300
            15.0f at 450
            -15.0f at 600
            15.0f at 750
            0.0f at 900
            15.0f at 1050
            -15.0f at 1200
            15.0f at 1350
            -15.0f at 1500
            15.0f at 1650
            -15.0f at 1800
            15.0f at 1950
            -15.0f at 2100
            15.0f at 2250
            -15.0f at 2400
            15.0f at 2550
            0.0f at 3000
        }
    )

    val offsetXState by animateDpAsState(
        targetValue = if (isPressed) offsetX else 0.dp,
        animationSpec = keyframes {
            durationMillis = 3000
            0.dp at 0
            offsetX at 3000
        }
    )

    val offsetYState by animateDpAsState(
        targetValue = if (isPressed) offsetY else 0.dp,
        animationSpec = tween(300)
    )

    var changeSize = animateFloatAsState(
        targetValue = if (!isPressed) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = 4000
            1.0f at 0
            1.0f at 2999
            0.0f at 3000
            1.0f at 4000
        }
    ).value

    DisposableEffect(isPressed) {
        onDispose {
            isPressed = false
        }
    }

    this
        .graphicsLayer(
            scaleX = changeSize,
            scaleY = changeSize,
            rotationZ = rotationOffset,
            translationX = offsetXState.value,
            translationY = offsetYState.value
        )
        .clickable {
            isPressed = true
        }
}


