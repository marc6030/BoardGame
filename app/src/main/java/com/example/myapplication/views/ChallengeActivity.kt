package com.example.myapplication


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.views.MenuScreen


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
                achievement(navController, viewModel, "Liked Games!", 30)
            }
            item {
                achievement(navController, viewModel, "Played Games!", 60)
            }
            item {
                achievement(navController, viewModel, "Logon Streak!", 7)
            }
            item {
                achievement(navController, viewModel, "Rated Games!", 26)
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
fun achievement(navController: NavHostController, viewModel: BoardDataViewModel, Headline: String, Completion: Int){
    //val item = viewModel.bigPictureGame
    val item: Painter = painterResource(id = R.drawable.banditachievement)
    val progress = Completion / 100f

    Box(modifier = Modifier
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
                    painter = item,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Spacer for additional distance
            Spacer(modifier = Modifier.width(8.dp))

            // Progress Indicator and Text
            Column {
                Text(text = Headline, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.onBackground)
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .height(10.dp)
                        .width(275.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.background
                )
                Text(text = "$Completion/100 ($Completion%)", fontSize = 15.sp, fontWeight = FontWeight.Normal, modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally), color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}


