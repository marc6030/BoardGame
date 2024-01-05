package com.example.myapplication.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.google.type.Fraction

// played games, liked games, something fun
    @Composable
fun PersonalActivity(navController: NavHostController) {
    val navBar = NavBar()
    val context = LocalContext.current
    val logo: Painter = painterResource(id = R.drawable.newbanditlogo)
    val icon: Painter = painterResource(id = R.drawable.search)
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = logo,
                contentDescription = null,
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp)
                    .align(Alignment.Center)
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            )
            Image(
                painter = icon,
                contentDescription = null, // Set a meaningful content description if needed
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .align(Alignment.TopEnd)
                    .padding(0.dp, 10.dp, 0.dp, 10.dp)
            )
        }
        Spacer(
            Modifier.height(20.dp)
        )
        Box(
            modifier = Modifier
                .size(100.dp, 100.dp)
                .background(Color.DarkGray, shape = CircleShape)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "?",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        KeyStats()
        Spacer(modifier = Modifier.height(10.dp))
        Menu(navController)
        Recents()
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        navBar.BottomNavigationBar(navController, "personal")
    }
}

@Composable
fun KeyStats(){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .height(70.dp)
        ) {
            keyStat()
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .height(70.dp)
        ) {
            keyStat()
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .height(70.dp)
        ) {
            keyStat()
        }
    }
}
@Composable
fun keyStat(){
    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                text = "-----",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize(),
                text = "-------",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun Menu(navController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .padding(end = 7.dp)
            ){
                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                        .clickable { navController.navigate("favorite") }
                ){
                    Text(
                        text = "Liked",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                ){
                    Text(
                        text = "pp",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 7.dp)
            ){

                Box(
                    modifier = Modifier
                        .padding(bottom = 3.dp)
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                ){
                    Text(
                        text = "pp",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxHeight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.DarkGray)
                ){
                    Text(
                        text = "pp",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun Recents(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.DarkGray)

        ){
            Text(
                text = "Recents",
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }
    }
}

