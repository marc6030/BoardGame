package com.example.myapplication.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var isCrying by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isCrying) {
        while (true) {
            delay(2000)
                isCrying = !isCrying
        }
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
        BanditAnimation(isCrying)
        Spacer(modifier = Modifier.height(16.dp))
        if(!isCrying){
            RefreshButton(onClick = {
                isCrying = !isCrying
            })
        } else {
            Text("", fontSize = 55.sp)
            if (isInternetAvailable(context)) {
                navController.navigate("home")
            }
        }
    }
}

@Composable
fun BanditAnimation(isCrying: Boolean) {
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
            .background(color = Color.Gray, shape = CircleShape)
            .padding(8.dp)
    ) {
        if(isCrying){
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
        Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Refresh")
    }
}
