package com.example.myapplication


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
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
        val boardSearchViewModel = BoardSearchViewModel()
        val boardGameInfoActivity = BoardGameInfoActivity(viewModel)
        val favoriteViewModel = FavoriteViewModel(viewModel)
        val playedGamesViewModel = PlayedGamesViewModel(sharedViewModel = viewModel)
        viewModel.checkOrCreateUser()
        boardDataViewModel.fetchBoardGameCategories()
        boardDataViewModel.getAllCategories()

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
                    playedGamesViewModel, account, navController, boardGameInfoActivity)
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
                 boardSearchViewModel: BoardSearchViewModel, playedGamesViewModel: PlayedGamesViewModel, account: GoogleSignInAccount?, navController: NavHostController,
                 boardGameInfoActivity: BoardGameInfoActivity) {

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
            val encodedCategory = navBackStackEntry.arguments?.getString("category")!!
            val category = encodedCategory.let { Uri.decode(it) }
            Log.v("currentCategory: ", "$category")
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