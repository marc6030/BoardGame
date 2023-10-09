package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import android.content.Intent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


class MainActivity : ComponentActivity() {

    private val mainScope = MainScope()

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }


    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}



