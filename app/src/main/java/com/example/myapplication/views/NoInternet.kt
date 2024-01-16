package com.example.myapplication.views

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.myapplication.R
import com.example.myapplication.isInternetAvailable
import kotlinx.coroutines.delay

@Composable
fun NoInternetScreen(navController: NavHostController) {
    var isSpinning by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isSpinning) {
        delay(2000)
        if(isInternetAvailable(context)){
            navController.navigate("home")
        }
        isSpinning = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Internet", fontSize = 40.sp)
        Spacer(modifier = Modifier.height(36.dp))
        BanditAnimation(isSpinning)
        Spacer(modifier = Modifier.height(16.dp))
        if(!isSpinning){
            RefreshButton(onClick = {
                isSpinning = true
            })
        } else {
            Text("", fontSize = 60.sp)
        }
    }
}

@Composable
fun BanditAnimation(isSpinning: Boolean) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bandit"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0f at 0 with FastOutLinearInEasing
                90f at 250 with FastOutLinearInEasing
                180f at 500 with FastOutLinearInEasing
                270f at 750 with FastOutLinearInEasing
                360f at 1000 with FastOutLinearInEasing
            },
            repeatMode = RepeatMode.Restart
        ), label = "bandit"
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(color = Color.Transparent)
            .padding(8.dp)
    ) {
        if(isSpinning){
            Image(
                painter = painterResource(id = R.drawable.newbanditlogo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .rotate(rotation)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.newbanditlogo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .scale(scale)
            )
        }
    }
}

@Composable
fun RefreshButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .height(50.dp),
    ) {
        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.Black)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Refresh", color = Color.Black)
    }
}
