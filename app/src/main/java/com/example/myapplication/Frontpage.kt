package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

class Frontpage {
    @Composable
    fun LogoImage(modifier: Modifier = Modifier){
        val image = painterResource(R.drawable.logo)

    }
}