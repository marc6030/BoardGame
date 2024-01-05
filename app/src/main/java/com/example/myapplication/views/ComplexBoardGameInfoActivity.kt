// ComplexBoardGameInfoActivity.kt
package com.example.myapplication


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.size.Size
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random


@Composable
fun ComplexBoardGameInfoActivity(
    navController: NavHostController,
    gameID: String?,
    boardDataViewModel: BoardDataViewModel,
    ratingsViewModel: RatingsViewModel,
    favoriteViewModel: FavoriteViewModel,
    sharedViewModel: SharedViewModel
    ) {
    val context = LocalContext.current

    // Use LaunchedEffect peoples! Is much importante!
    LaunchedEffect(gameID) {
        boardDataViewModel.fetchBoardGameData(gameID!!)
        ratingsViewModel.fetchRatings(gameID!!)
        favoriteViewModel.fetchFavoriteListFromDB()

        delay(800)
        sharedViewModel.firstAnimationComplexBoardInfo = true
        delay(1000)
        sharedViewModel.secondAnimationComplexBoardInfo = true

        // viewModel.isBoardGameFavourite(gameID)
        Log.v("Fetch Game ID in boardgamedata", "$gameID")

    }

    val colorMatrix = ColorMatrix().apply {
        setToScale(0.2f, 0.2f, 0.2f, 1f)
    }


    val isLoading = sharedViewModel.isLoading
    val boardGame = sharedViewModel.boardGameData
    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()

    if (gameID != null) {
        if (isLoading) {
            // Indikator
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
            var selectedTabIndex by remember { mutableStateOf(0) }

            // Observe the data
            if (boardGame != null) {
                AnimatedVisibility(
                    sharedViewModel.firstAnimationComplexBoardInfo,
                    enter = EnterTransition.Companion.None,
                    exit = ExitTransition.None
                ) {
                    AsyncImage(
                        model = boardGame.imageURL,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(30.dp)
                            .scale(if (sharedViewModel.firstAnimationComplexBoardInfo) 1.5f else 0.3f)
                            .animateContentSize(),
                        colorFilter = ColorFilter.colorMatrix(colorMatrix)
                    )
                }
                AnimatedVisibility(
                    sharedViewModel.secondAnimationComplexBoardInfo,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = boardGame!!.name,
                            style = TextStyle(
                                fontSize = 50.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            textAlign = TextAlign.Center
                        )

                        pictureAndKeyInfo(boardGame!!)

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Column() {
                                tabView(
                                    texts = listOf(
                                        "Description",
                                        "General Info",
                                        "BoardBandit Rating"
                                    )
                                ) {
                                    selectedTabIndex = it;
                                }
                                when (selectedTabIndex) {
                                    0 -> description(
                                        boardGame!!
                                    )

                                    1 -> generalInfo(
                                        boardGame!!
                                    )

                                    2 -> ratingTab(
                                        boardGame!!, ratingsViewModel
                                    )
                                }
                            }
                        }
                    }
                }
                favoriteButton(navController, favoriteViewModel, sharedViewModel, boardgameID = boardGame.id)
            }
        }
    }
}


@Composable
fun pictureAndKeyInfo(boardGame: BoardGame){
    Row(
        modifier = Modifier.height(175.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(10.dp)
        ) {
            AsyncImage(
                model = boardGame.imageURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )

        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.background),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
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
                    )
                    Text(
                        text = "${boardGame.minPlayers} - ${boardGame.maxPlayers}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.av_timer),
                        contentDescription = null
                    )
                    Text(
                        text = "${boardGame.playingTime} min.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
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
                        contentDescription = null
                    )
                    Text(
                        text = "${boardGame.age}+",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Image(
                        painter = painterResource(id = R.drawable.fitness_center),
                        contentDescription = null
                    )
                    Text(
                        text = boardGame.averageWeight,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}

@Composable
fun tabView(
    modifier: Modifier = Modifier,
    texts: List<String>,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    val inactiveColor = Color(0xFF777777)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier
    ) {
        texts.forEachIndexed { index, item ->
            Tab(
                modifier = modifier.background(MaterialTheme.colorScheme.background),
                selected = selectedTabIndex == index,
                selectedContentColor = MaterialTheme.colorScheme.background,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    selectedTabIndex = index
                    onTabSelected(index)
                }
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun description(boardGame: BoardGame) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            Box {
                Text(
                    text = boardGame.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun generalInfo(boardGame: BoardGame) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        item {
            simpleInfo("Player Count", boardGame.minPlayers, boardGame.maxPlayers)
            simpleInfo("Weight", boardGame.averageWeight, null)
            simpleInfo("BGG Rank", boardGame.overallRank, null)
            simpleInfo("Time", boardGame.playingTime, null)
            simpleInfo("Age", boardGame.age+"+", null)
            simpleInfo("BGG Rating", boardGame.ratingBGG, null)
            /*if(boardGame.category != "") {
                simpleInfo(boardGame.category, info1 = boardGame.categoryRank, info2 = null)
            }
             */
            complexInfo(title = "Mechanisms", infoList = boardGame.mechanisms)
            complexInfo(title = "Categories", infoList = boardGame.categories)
            complexInfo(title = "Publishers", infoList = boardGame.publishers)
            complexInfo(title = "Artists", infoList = boardGame.artists)
            complexInfo(title = "Designers", infoList = boardGame.designers)
            complexInfo(title = "Families", infoList = boardGame.families)
        }
    }
}

@Composable
fun simpleInfo(title: String, info1: String, info2: String?) {
    Box{
        Row(modifier = Modifier
            .fillMaxWidth()) {
            Text(
                text = title + ": ",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(0.65f),
                fontSize = 20.sp
            )
            if (info2 != null) {
                Text(
                    text = info1 + " - " + info2,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = info1,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(1f),
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun complexInfo(title: String, infoList : List<String>) {
    var halfway = kotlin.math.floor((infoList.size / 2).toDouble())
    if(infoList.size % 2 == 0){
        halfway = halfway - 1
    }
    Box {
        Column {
            Text(
                text = title + ":",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth(0.5f),
                fontSize = 20.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    for (i in 0..(halfway).toInt()) {
                        Text(
                            text = infoList.get(i),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Left,
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }
                }
                Column {
                    for (i in (halfway + 1).toInt() until infoList.size) {
                        Text(
                            text = infoList.get(i),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ratingTab(boardGame: BoardGame, viewModel: RatingsViewModel){
    val averageRating = viewModel.averageRating
    Column {
        starDisplay(boardGame.ratingBGG, "BGG rating")
        starDisplay(averageRating.toString(), text ="BoardBandit Average Rating")
        ratingDisplay(text = "Your Rating" , viewModel =viewModel , boardGame =  boardGame)
        Log.v("BGG Rating", "${boardGame.ratingBGG}")
    }
}

@Composable
fun starDisplay(stars: String, text: String){
    val num_of_stars: Double = stars.toDouble()
    Column {
        Box {
            Text(text + ": $num_of_stars / 10")
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentWidth(Alignment.Start)
        ) {
            Row() {
                for (i in 1..10) {
                    if (num_of_stars >= i) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorite Icon",
                            tint = Color.Yellow,
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
fun ratingDisplay(text: String,
                  viewModel: RatingsViewModel,
                  boardGame: BoardGame){
    var num_of_stars = 0.0
    val userRating = viewModel.userRating
    if(userRating == ""){
        num_of_stars = 0.0
    }else{
        num_of_stars = userRating!!.toDouble()
    }
    Column {
        Box {
            Text(text + ": $num_of_stars / 10 - Rate by tapping a Star")
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
                .wrapContentWidth(Alignment.Start)
        ) {
            Row() {
                for (i in 1..10) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Ratings Icon",
                            tint = if(num_of_stars >= i) Color.Yellow else Color.Black,
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { viewModel.toggleRatings(boardGame, i.toString()) }
                            // .border(BorderStroke(2.dp, color = Color.Black), 2.dp, Shape = ShapeTokens.BorderDefaultShape)
                        )
                    }
                }
            }
        }
    }


@Composable
fun favoriteButton(
    navController: NavHostController,
    viewModel: FavoriteViewModel,
    sharedViewModel: SharedViewModel,
    boardgameID : String
) {

        var triggerConfetti by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    sharedViewModel.secondAnimationComplexBoardInfo = false
                    sharedViewModel.firstAnimationComplexBoardInfo = false
                    navController.navigate(sharedViewModel.goBackToElseThanInfo)
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(8.dp),

                ) {

            }
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(18.dp)
            )
            Icon(
                imageVector = if (
                    sharedViewModel.boardGameData!!.isfavorite) Icons.Outlined.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite Icon",
                tint = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(55.dp)
                    .padding(8.dp)
                    .clickable {
                        triggerConfetti = !triggerConfetti
                        viewModel.toggleFavorite(sharedViewModel.boardGameData!!)
                        Log.v("is still fav", "${sharedViewModel.boardGameData!!.isfavorite}")
                    }
            )
            if (triggerConfetti) {
                ParticleSystem(
                    18.dp,
                    15.dp,
                    200,
                    modifier = Modifier.align(Alignment.CenterEnd).size(55.dp).padding(8.dp)
                ) // Confetti
            }
        }
    }


    data class Particle(
        val position: Offset,
        val velocity: Offset,
        val acceleration: Offset,
        val color: Color,
        val size: Float,
        val isCircle: Boolean
    )

    @Composable
    fun ParticleSystem(posXInDp: Dp, posYInDp: Dp, size: Int, modifier: Modifier) {

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

        var counter = 0 // Counts how many iterations each particle has been updated

        // Updates each particle pos, vel, acc, size and alpha
        LaunchedEffect(Unit) {
            while (true) {
                val particlesCopy = ArrayList(mutableParticles.map { it.copy() })
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
                        size = androidx.compose.ui.geometry.Size(
                            particle.size,
                            particle.size
                        )
                    )
                }
            }
        }
    }


