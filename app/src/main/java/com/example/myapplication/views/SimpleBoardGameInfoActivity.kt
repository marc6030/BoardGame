package com.example.myapplication


import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel

@Composable
fun SimpleBoardGameInfoActivity(navController: NavHostController,
        boardDataViewModel: BoardDataViewModel,
        ratingsViewModel: RatingsViewModel,
        favoriteViewModel: FavoriteViewModel,
        sharedViewModel: SharedViewModel
) {
    //val context = LocalContext.current

    // Use LaunchedEffect peoples! Is much importante!
    // boardDataViewModel.fetchBoardGameData(sharedViewModel.currentGameID)
    ratingsViewModel.fetchRatings(sharedViewModel.currentGameID)
    favoriteViewModel.fetchFavoriteListFromDB()


    val colorMatrixDark = ColorMatrix().apply {
        setToScale(0.2f, 0.2f, 0.2f, 1f)
    }

    var boardGame = sharedViewModel.boardGameData // It IS a var. It will not work as intended as a val. Trust me bro

    // val boardGameIsFavourite by viewModel.isBoardGameFavourite.observeAsState()
    AsyncImage(
        model = boardGame.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .blur(30.dp)
            //.scale(if (sharedViewModel.firstAnimationSimpleBoardInfo) 1.5f else 0.3f)
            .animateContentSize(),
        colorFilter = ColorFilter.colorMatrix(colorMatrixDark)
    )


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
            Text(
                text = boardGame!!.name,
                style = TextStyle(
                    fontSize = 50.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                textAlign = TextAlign.Center,
                color = Color.White
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
                .align(Alignment.TopCenter)){
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "contentDescription",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.BottomStart),
                tint = Color.DarkGray)
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "contentDescription",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.BottomEnd),
                tint = Color.DarkGray)
        }
        Row(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.85f)
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "3.5",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.Bottom),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "3.5",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .align(Alignment.Bottom),
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Box(modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(1f)
            .fillMaxHeight(0.963f)
            .align(Alignment.TopCenter)){
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = "contentDescription",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.DarkGray, CircleShape)
                    .align(Alignment.BottomCenter)
                    .clickable { navController.navigate("complexBoardgameinfo/${boardGame.id}")},
                tint = Color.White,)
        }
    }
    Button(
        onClick = {
            // navController.navigate(sharedViewModel.goBackToElseThanInfo)
        },
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
    }
    Image(
        painter = painterResource(id = R.drawable.ic_action_name),
        contentDescription = null,
        modifier = Modifier
            .padding(18.dp)
    )
}



