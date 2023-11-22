// BoardGameInfoActivity.kt
package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.MyViewModel


@Composable
fun BoardGameInfoActivity(navController: NavHostController, gameID: String?, viewModel: MyViewModel) {
    val context = LocalContext.current
    if (gameID != null) {
        // Check internet Connection
        // Emil comment - maybe a bit much checking it everytime we load an item? - also we don't handle retries anyways?
        if (!isInternetAvailable(context)) {
            Text("No Internet!")
        }
        // Use LaunchedEffect peoples! Is much importante!
        LaunchedEffect(Unit) {
            viewModel.fetchBoardGameData(gameID)
        }

        val isLoading by viewModel.isLoading.observeAsState(initial = false)

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
            val boardGame = viewModel.boardGameData.value
            // Observe the data
            if (boardGame != null) {
                // Data is available, update the UI
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = boardGame.name,
                        style = TextStyle(
                            fontSize = 50.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )
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
                                .background(Color.LightGray),

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


                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                    ) {
                        Column() {
                            Text(
                                text = "Description:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )

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
                    }


                }
                Button(
                    onClick = {
                        navController.popBackStack()
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
                        .padding(18.dp),


                    )

            }
        }
    }
}

