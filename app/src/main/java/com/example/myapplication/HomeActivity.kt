package com.example.myapplication


import android.content.ClipData
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun HomeActivity(navController: NavHostController, viewModel: MyViewModel) {
    val navBar = NavBar()
    val url = "https://boardgamegeek.com/xmlapi2/hot?boardgame"
    val context = LocalContext.current
    // Check internet Connection
    if (!isInternetAvailable(context)) {
        Text("No Internet!")
    }

    viewModel.fetchBoardGameList(url) // Henter dataen

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    if (isLoading) {
        Row (
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

        val items = viewModel.boardGameDataList.value
        if(items != null) {
            Box(modifier = Modifier
                .fillMaxSize()
            ){
            LazyColumn( modifier = Modifier)
            {
                item {
                    Box(
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier
                                .height(80.dp)
                                .width(80.dp)
                                .align(Alignment.Center)
                        )
                    }

                    Text(text = "Headline1", fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp, top = 20.dp))
                    LazyRow(
                        modifier = Modifier
                    )
                    {
                        items(items.boardGames) { item ->
                            Box(
                                modifier = Modifier
                                    .size(175.dp)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                            {
                                AsyncImage(
                                    model = item.imgUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth(1f)
                                        .fillMaxHeight(0.33f)
                                        .background(
                                            Color.LightGray,
                                            shape = MaterialTheme.shapes.large
                                        )
                                ) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }

                        }
                    }

                    Text(text = "Headline2", fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp, top = 20.dp))
                    LazyRow(
                        modifier = Modifier
                    )
                    {
                        items(items.boardGames) { item ->
                            Box(
                                modifier = Modifier
                                    .size(175.dp)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                            {
                                AsyncImage(
                                    model = item.imgUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth(1f)
                                        .fillMaxHeight(0.33f)
                                        .background(
                                            Color.LightGray,
                                            shape = MaterialTheme.shapes.large
                                        )
                                ) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }

                        }
                    }

                    Text(text = "Headline3", fontSize = 35.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp, top = 20.dp))
                    LazyRow(
                        modifier = Modifier
                    )
                    {
                        items(items.boardGames) { item ->
                            Box(
                                modifier = Modifier
                                    .size(175.dp)
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                            {
                                AsyncImage(
                                    model = item.imgUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth(1f)
                                        .fillMaxHeight(0.33f)
                                        .background(
                                            Color.LightGray,
                                            shape = MaterialTheme.shapes.large
                                        )
                                ) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }

                        }
                    }

                }
            }
            Box( modifier = Modifier
                .align(Alignment.BottomCenter)
            ) {
                navBar.BottomNavigationBar(navController, "Home")
            }
            }
        }
    }
}

//                    items(items.boardGames) { item ->
//                        val gameName: String = item.name
//                        val gameID: String = item.id
//                        Button(
//                            onClick = {
//                                navController.navigate("boardgameinfo/$gameID")
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                        ) {
//                            Text(gameName)
//                        }
//                    }
