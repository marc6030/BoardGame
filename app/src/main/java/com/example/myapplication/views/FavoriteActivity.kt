package com.example.myapplication


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.myapplication.modelviews.MyViewModel
import com.example.myapplication.views.NavBar


@Composable
fun FavoriteActivity(navController: NavHostController, viewModel: MyViewModel) {
    val logo: Painter = painterResource(id = R.drawable.banditlogo)

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
        ){
            viewModel.boardGameDataList.value?.let {
                items(it.boardGames) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate("boardgameinfo/${item.id}") }
                    ) {
                        AsyncImage(
                            model = item.imgUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                        Text(text = item.name, modifier = Modifier.align(Alignment.Center), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        NavBar().BottomNavigationBar(navController, "Favorite")
    }
}
