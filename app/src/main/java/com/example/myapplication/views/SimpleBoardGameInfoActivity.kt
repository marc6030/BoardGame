package com.example.myapplication


import androidx.compose.animation.animateContentSize
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
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
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.views.YoutubePlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import BoardGameRepository
import android.util.Log
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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

    LaunchedEffect(Unit) {
        // Use LaunchedEffect peoples! Is much importante!
        boardGameInfoActivity.fetchBoardGameData(gameID)
    }


    val colorMatrixDark = ColorMatrix().apply {
        setToScale(0.2f, 0.2f, 0.2f, 1f)
    }

    val boardGame =
        boardGameInfoActivity.boardGameData // It IS a var. It will not work as intended as a val. Trust me bro
    val textStyleBody1 = MaterialTheme.typography.headlineLarge.copy(fontSize = 50.sp)
    var textStyle by remember { mutableStateOf(textStyleBody1) }
    var readyToDraw by remember { mutableStateOf(false) }

    boardGameInfoActivity.fetchYoutubeID(boardGame.name)



    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()
    DisposableEffect(boardGame!!.name) {
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
            .blur(30.dp)
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
                                        .padding(20.dp, 30.dp, 20.dp, 20.dp)
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
                                    IconButton(
                                        onClick = { showYouTubePlayer = true },
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(80.dp)
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
                                        YoutubePlayer(youtubeVideoId = boardGameInfoActivity.youtubeID, lifecycleOwner = LocalLifecycleOwner.current)

                                        // Close Button
                                        IconButton(
                                            onClick = { showYouTubePlayer = false},
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(16.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
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
                                    ){
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
                                        .clickable { boardGameInfoActivity.openRatingPopUp = !boardGameInfoActivity.openRatingPopUp
                                        },
                                    tint = Color.DarkGray
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
                                boardGameInfoActivity = boardGameInfoActivity,
                                viewModel = ratingsViewModel
                            )
                        }
                        /*AnimatedVisibility(
                            visible = boardGameInfoActivity.openAddPopUp,
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        ) {
                            PopupAddDialog()
                        }*/
                        AddToChallengeButton(boardGameInfoActivity = boardGameInfoActivity)
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
                        .background(
                            Color.DarkGray,
                            RoundedCornerShape(10.dp)
                        )
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

@Composable
fun ratingTab(boardGame: BoardGame, viewModel: RatingsViewModel) {
    val averageRating = viewModel.averageRating
    Column {
        starDisplay(boardGame.ratingBGG, "BGG rating")
        starDisplay(
            averageRating.toString(),
            text = "BoardBandit Average Rating"
        )
        ratingDisplay(
            text = "Your Rating",
            viewModel = viewModel,
            boardGame = boardGame
        )
        Log.v("BGG Rating", "${boardGame.ratingBGG}")
    }
}

@Composable
fun starDisplay(stars: String, text: String) {
    val numOfStars: Double = stars.toDouble()
    Column {
        Box {
            Text(text + ": $numOfStars / 10")
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
                            tint = Color.White,
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
                    boardGame: BoardGame
                ) {
    var numOfStars = 0.0
    val userRating = viewModel.userRating
    if (userRating == "") {
        numOfStars = 0.0
    } else {
        numOfStars = userRating!!.toDouble()
    }
    Column {
        Box {
            Text(text + ": $numOfStars / 10 - Rate by tapping a Star")
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentWidth(Alignment.Start)
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
                                viewModel.toggleRatings(
                                    boardGame,
                                    i.toString()
                                )
                            }
                        // .border(BorderStroke(2.dp, color = Color.Black), 2.dp, Shape = ShapeTokens.BorderDefaultShape)
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
            .fillMaxWidth(0.852f)
            .fillMaxHeight(0.75f)
    ) {
        Icon(imageVector = if (boardGameInfoActivity.boardGameData!!.isfavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "favoriteButton",
            tint = Color.White,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent, CircleShape)
                .align(Alignment.BottomEnd)
                .clickable {
                    triggerConfetti = !triggerConfetti
                    //boardGameInfoActivity.toggleFavorite("static_user", boardGameInfoActivity.boardGameData!!.id)
                    boardGameInfoActivity.snackbarFavoriteVisible =
                        !boardGameInfoActivity.snackbarFavoriteVisible
                    Log.v(
                        "is still fav",
                        "${boardGameInfoActivity.boardGameData!!.isfavorite}"
                    )
                    if (boardGameInfoActivity.snackbarFavoriteVisible) {
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = if (/*boardGameInfoActivity.boardGameData.isfavorite*/ true) "Added to My Games" else "Removed from My Games",
                                actionLabel = "UNDO",
                                duration = SnackbarDuration.Short,
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                //boardGameInfoActivity.toggleFavorite("static_user", boardGameInfoActivity.boardGameData.id)
                            }
                            boardGameInfoActivity.snackbarFavoriteVisible =
                                false
                        }
                    }
                }
        )
        if (triggerConfetti) {
            ParticleSystem(
                18.dp,
                15.dp,
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
    // For-loop that creates each individual particle and adds it to particles
    for (i in 1..size) {
        // Colors that particle can have
        val colors = listOf(
            Color(255, 0, 0, 255),
            Color(0, 255, 0, 255),
            Color(0, 0, 255, 255),
            Color(255, 152, 0, 255),
            Color(255, 235, 59, 255)
        )

        // Adding the particle to particles
        particles.add(
            Particle(
                Offset(posX.toFloat(), posY.toFloat()),
                Offset(1F, 1F),
                Offset(0F, 0F),
                colors[Random.nextInt(colors.size)],
                10f,
                Random.nextBoolean()
            )
        )
    }

    // Add all particles to a mutable state of particles
    val mutableParticles = remember { mutableStateListOf<Particle>() }
    mutableParticles.addAll(particles)

    var counter =
        0 // Counts how many iterations each particle has been updated

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

            delay(16L) // Delay before next iteration
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
fun AddToChallengeButton(boardGameInfoActivity: BoardGameInfoActivity) {
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
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable {
                        boardGameInfoActivity.snackbarChallengeVisible =
                            !boardGameInfoActivity.snackbarChallengeVisible
                        if (boardGameInfoActivity.snackbarChallengeVisible) {
                            coroutineScope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Congratulation on playing this game! Added to Challenges",
                                    actionLabel = "UNDO",
                                    duration = SnackbarDuration.Short,
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    //boardGameInfoActivity.(REMOVEFROMCHALLENGE)
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
