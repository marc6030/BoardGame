package com.example.myapplication


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardGameInfoActivity


@Composable
fun ChallengeActivity(navController: NavHostController, viewModel: BoardDataViewModel, boardGameInfoActivity: BoardGameInfoActivity) {

    val scrollState = rememberLazyListState()


    val gradientFrom = MaterialTheme.colorScheme.surface
    val gradientTo = MaterialTheme.colorScheme.background
    Column(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    Brush.verticalGradient(
                        colorStops = arrayOf(0f to gradientFrom, 1f to gradientTo),
                        tileMode = TileMode.Decal
                    )
                )
            }
    ) {
        Spacer(Modifier.height(40.dp))
        Text(
            text = "Challenges",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            item {
                achievement("Liked Games! - Bronze ", viewModel.nrOfLikedGames.toInt(), 100f)
            }
            item {
                achievement("Liked Games! - Silver", viewModel.nrOfLikedGames.toInt(), 250f)
            }
            item {
                achievement("Liked Games! - Gold", viewModel.nrOfLikedGames.toInt(), 500f)
            }
            item {
                achievement("Played Games! - Bronze", viewModel.nrOfPlayedGames.toInt(), 100f)
            }
            item {
                achievement("Played Games! - Silver", viewModel.nrOfPlayedGames.toInt(), 250f)
            }
            item {
                achievement("Played Games! - Gold", viewModel.nrOfPlayedGames.toInt(), 500f)
            }
            item {
                achievement("Logon Streak! - Bronze", viewModel.streak.toInt(), 100f)
            }
            item {
                achievement("Logon Streak! - Silver", viewModel.streak.toInt(), 250f)
            }
            item {
                achievement("Logon Streak! - Gold", viewModel.streak.toInt(), 500f)
            }
            item {
                achievement("Rated Games! - Bronze", viewModel.nrOfRatedGames.toInt(), 100f)
            }
            item {
                achievement("Rated Games! - Silver", viewModel.nrOfRatedGames.toInt(), 250f)
            }
            item {
                achievement("Rated Games! - Gold", viewModel.nrOfRatedGames.toInt(), 500f)
            }
        }

        }
    IconButton(
        onClick = { navController.popBackStack() }
    ){
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft,
            contentDescription = "back arrow",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun achievement(Headline: String, Completion: Int, Bar: Float) {
    //val item = viewModel.bigPictureGame
    val achievement: Painter
    if (Bar == 100f) {
        achievement = painterResource(id = R.drawable.bronzeachiev)
    } else if (Bar == 250f) {
        achievement = painterResource(id = R.drawable.silverachiev)
    } else {
        achievement = painterResource(id = R.drawable.goldachiev)
    }

    val progress = Completion / Bar
    var percent = (progress * 100).toInt()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between elements in the Row
        ) {
            // Image Box
            Box(
                modifier = Modifier
                    .size(125.dp)
            ) {
                Image(
                    painter = achievement,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Spacer for additional distance
            Spacer(modifier = Modifier.width(8.dp))

            // Progress Indicator and Text
            Column {
                Text(
                    text = Headline,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground
                )
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(10.dp)
                        .width(275.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.background
                )
                Text(
                    text = "$Completion/${Bar.toInt()} (${percent}%)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}




