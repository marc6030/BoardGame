package com.example.myapplication


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.views.NavBar



@Composable
fun FavoriteActivity(navController: NavHostController, viewModel: FavoriteViewModel, boardGameInfoActivity: BoardGameInfoActivity) {
    val logo: Painter = painterResource(id = R.drawable.banditlogo)

    LaunchedEffect(Unit) {
        viewModel.fetchFavoriteBoardGames()
    }
    val favoriteBoardGames = viewModel.favoriteBoardGameList
    Log.v("Favorites: ", "$favoriteBoardGames")



    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
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
                .background(MaterialTheme.colorScheme.background)
        )
        Text(
            text = "Favorites",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(12.dp),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
            )
        LazyColumn (modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(MaterialTheme.colorScheme.background)
        ) {
            items(favoriteBoardGames) { boardgame ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(100.dp)
                                .width(100.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    navController.navigate("boardgameinfo/${boardgame.id}")
                                },
                            contentAlignment = Alignment.TopCenter
                        ) {
                            AsyncImage(
                                model = boardgame.imgUrl,
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                            )
                        }
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Text(
                                shortTitel(boardgame.name),
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.clickable {
                                    navController.navigate("boardgameinfo/${boardgame.id}")
                                }
                            )
                        }
                        Spacer(modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.background)
                            .clickable {
                                navController.navigate("boardgameinfo/${boardgame.id}")
                            }
                        )
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Favorite Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { boardGameInfoActivity.toggleFavorite(boardgame.id) }
                        )
                        Spacer(Modifier.width(10.dp))
                    }

            }
        }
        NavBar().BottomNavigationBar(navController, "Favorite")
    }

}

fun shortTitel(name: String): String{
    val index = name.indexOf(":")
    return if (index != -1) {
        name.substring(0, index)
    } else {
        name
    }
}
