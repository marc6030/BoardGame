package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        var myViewModel : MyViewModel = MyViewModel()
        // val intent = Intent(this, HomeActivity::class.java)
        // startActivity(intent)
        setContent{
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeActivity(navController)
                }
                composable("search") {
                    SearchActivity(navController)
                }
                composable("favorite") {
                    FavoriteActivity(navController)
                }
                composable(
                    route = "boardgameinfo/{gameID}",
                    arguments = listOf(navArgument("gameID") { type = NavType.StringType })
                ) { backStackEntry ->
                    val arguments = requireNotNull(backStackEntry.arguments)
                    val gameID = arguments.getString("gameID")
                    BoardGameInfoActivity(navController, gameID, myViewModel)
                }
            }
        }
    }
}




