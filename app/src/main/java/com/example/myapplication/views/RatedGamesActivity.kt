package com.example.myapplication.views

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.RatingsViewModel


@Composable
fun RatedGamesActivity(navController: NavHostController, viewModel: RatingsViewModel) {

    val scrollState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            if (viewModel.ratedGamesList.size > 8) {
                viewModel.fetchAdditionalRatedBoardGames()
            }

        }
    }

    LaunchedEffect(Unit){
        viewModel.fetchRatedBoardGames()
    }

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=32.dp)
        ) {
            IconButton(
                onClick = {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    if (currentRoute != "personal") {
                        navController.popBackStack()}

                          },
                modifier = Modifier.align(Alignment.CenterStart).size(50.dp),

                ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "back arrow",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(50.dp)
                )
            }
            Text(
                text = "Rated Games",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyle(
                    shadow = Shadow(color = Color.Black, blurRadius = 6f)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                state = scrollState
            ) {
                items(viewModel.ratedGamesList) { boardgame ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                navController.navigate("boardgameinfo/${boardgame.id}")
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AsyncImage(
                                model = boardgame.imgUrl,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = shortTitel(boardgame.name),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(end = 1.dp)
                            )
                        }
                        Text(text = boardgame.rating + "/10",
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onBackground)
                        Icon(imageVector = Icons.Filled.Star, contentDescription = "star",
                            modifier = Modifier.size(30.dp),
                            tint = MaterialTheme.colorScheme.onBackground)
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


