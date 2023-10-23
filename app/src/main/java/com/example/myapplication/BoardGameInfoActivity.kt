// BoardGameInfoActivity.kt
package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage


@Composable
fun BoardGameInfoActivity(navController: NavHostController, gameID: String?, viewModel: MyViewModel) {
    val context = LocalContext.current
    if (gameID != null) {
        // Check internet Connection
        if (!isInternetAvailable(context)) {
            Text("No Internet!")
        }

        if(gameID != null){
            viewModel.fetchBoardGameData(gameID)
        } else {
            viewModel.fetchBoardGameData("2536")
        }

        val isLoading by viewModel.isLoading.observeAsState(initial = false)

        if (isLoading) {
            // Indikator
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
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.height(150.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .height(150.dp)
                        ) {
                            AsyncImage(
                                model = boardGame.imageURL,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp))
                            )

                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            Column (
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            ){
                                Text(
                                    text = "Year Published: ${boardGame.yearPublished}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    text = "Players: ${boardGame.minPlayers} - ${boardGame.maxPlayers}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    text = "Playing Time: ${boardGame!!.playingTime} mins",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(
                                    text = "Age: ${boardGame.age}+",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Spacer(modifier = Modifier.height(8.dp))


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Description:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyColumn(modifier = Modifier.weight(1f)){
                       item{
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
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .padding(8.dp)
                ) {
                    Text("Back")
                }
            }
        }
    }
}
