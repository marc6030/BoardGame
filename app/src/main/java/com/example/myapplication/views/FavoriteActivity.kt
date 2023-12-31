package com.example.myapplication


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.NavBar



@Composable
fun FavoriteActivity(navController: NavHostController, viewModel: FavoriteViewModel, sharedViewModel: SharedViewModel) {
    val logo: Painter = painterResource(id = R.drawable.banditlogo)


    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteListFromDB()
    }

    val favoriteBoardGame = sharedViewModel.favoriteBoardGameList

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Image(
                painter = logo,
                contentDescription = null, // Set a meaningful content description if needed
                modifier = Modifier
                    .height(120.dp)
                    .width(120.dp)
                    .align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )
        Text(text = "Your liked games!", modifier = Modifier.padding(12.dp), fontSize = 26.sp, fontWeight = FontWeight.Bold)
        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Color.White)
        ) {
            if (favoriteBoardGame != null) {
                items(favoriteBoardGame) { boardgame ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                if (boardgame != null) {
                                    navController.navigate("boardgameinfo/${boardgame.id}")
                                }
                            }
                    ) {
                        if (boardgame != null) {
                            AsyncImage(
                                model = boardgame.imageURL,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxWidth(1f)
                                    .fillMaxHeight(0.5f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(1f)
                                .fillMaxHeight(0.5f)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                        {
                            if (boardgame != null) {
                                Text(
                                    text = boardgame.shortTitel(),
                                    modifier = Modifier.align(Alignment.Center),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Favorite Icon",
                                tint = Color.Red,
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .size(32.dp)
                                    .clickable { viewModel.toggleFavorite(boardgame) }
                            )
                        }
                    }

                }
            }
        }

        NavBar().BottomNavigationBar(navController, "Favorite")
    }
}