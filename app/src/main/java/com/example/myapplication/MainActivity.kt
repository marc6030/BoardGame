package com.example.myapplication


import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.BoardSearchViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.PlayedGamesViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.CategoryActivity
import com.example.myapplication.views.NoInternetScreen
import com.example.myapplication.views.PersonalActivity
import com.example.myapplication.views.PlayedGamesActivity
import com.example.myapplication.views.RatedGamesActivity
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    val viewModel: SharedViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val ratingsViewModel = RatingsViewModel(viewModel)
        val boardDataViewModel = BoardDataViewModel(viewModel)
        val boardSearchViewModel = BoardSearchViewModel(viewModel)
        val boardGameInfoActivity = BoardGameInfoActivity(viewModel)
        val favoriteViewModel = FavoriteViewModel(viewModel, boardGameInfoActivity)
        val playedGamesViewModel = PlayedGamesViewModel(sharedViewModel = viewModel,
            boardGameInfoActivity = boardGameInfoActivity
        )

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            navController = rememberNavController()
            AppTheme(useDarkTheme = true) {
                boardgameApp(favoriteViewModel, ratingsViewModel, boardDataViewModel, boardSearchViewModel,
                    viewModel, playedGamesViewModel, account, navController, boardGameInfoActivity)
            }
        }
    }
    override fun onBackPressed() {
        if (navController.currentBackStackEntry?.destination?.route == "home") {
            super.onBackPressed()
        } else {
            navController.popBackStack()
        }
    }
}


@Composable
fun boardgameApp(favoriteViewModel: FavoriteViewModel, ratingsViewModel: RatingsViewModel, boardDataViewModel: BoardDataViewModel,
                 boardSearchViewModel: BoardSearchViewModel,sharedViewModel: SharedViewModel, playedGamesViewModel: PlayedGamesViewModel, account: GoogleSignInAccount?, navController: NavHostController,
                 boardGameInfoActivity: BoardGameInfoActivity) {

    val transitionDuration = 1000 // ms
    val context = LocalContext.current
    LaunchedEffect(true){
        while(true){
            if(!isInternetAvailable(context)){
                navController.navigate("nointernet")
            }
            delay(5000)
        }
    }
    NavHost(
        navController = navController,
        startDestination = "home"

    ) {
        composable("nointernet") {
            NoInternetScreen(navController)
        }
        composable(
            route = "home")
        {
            HomeActivity(navController, boardDataViewModel)
        }
        composable(
            route = "search") {
            searchActivity(navController, boardSearchViewModel, boardGameInfoActivity)


        }
        composable(
            route = "favorite") {
            FavoriteActivity(navController, favoriteViewModel, boardGameInfoActivity)
        }
        composable(
            route = "category/{category}") {
                navBackStackEntry ->
            val category = navBackStackEntry.arguments?.getString("category")!!
            CategoryActivity(
                navController = navController,
                viewModel = boardDataViewModel,
                category = category
            )
        }
        composable(
            route = "challenge") {
            ChallengeActivity(navController, boardDataViewModel, boardGameInfoActivity)
        }
        composable(
            route = "playedGames") {
            PlayedGamesActivity(navController, playedGamesViewModel, boardGameInfoActivity)
        }
        composable(
            route = "ratedGames") {
            RatedGamesActivity(navController, ratingsViewModel, boardGameInfoActivity)
        }
        composable(
            route = "personal"
        ) {
            PersonalActivity(navController, viewModel = boardDataViewModel)
        }

        composable(
            route = "boardgameinfo/{gameID}")
        {navBackStackEntry ->
            val gameID = navBackStackEntry.arguments?.getString("gameID")!!
            SimpleBoardGameInfoActivity(
                navController,
                ratingsViewModel,
                boardGameInfoActivity,
                gameID
            )
        }
    }
}