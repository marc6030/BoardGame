// ComplexBoardGameInfoActivity.kt
package com.example.myapplication


import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
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
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

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
