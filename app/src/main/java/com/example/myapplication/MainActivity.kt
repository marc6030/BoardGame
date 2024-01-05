package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.AppTheme
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardSearchViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.NavBar
import com.example.myapplication.views.NoInternetScreen
import com.example.myapplication.views.PersonalActivity
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    val viewModel: SharedViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val ratingsViewModel = RatingsViewModel(viewModel)
        val boardDataViewModel = BoardDataViewModel(viewModel)
        val boardSearchViewModel = BoardSearchViewModel(viewModel)
        val favoriteViewModel = FavoriteViewModel(viewModel)

        setContent {
            AppTheme(useDarkTheme = true) {
                boardgameApp(favoriteViewModel, ratingsViewModel, boardDataViewModel, boardSearchViewModel,
                    viewModel, account)
            }
        }
    }
}

@Composable
fun boardgameApp(favoriteViewModel: FavoriteViewModel, ratingsViewModel: RatingsViewModel, boardDataViewModel: BoardDataViewModel, boardSearchViewModel: BoardSearchViewModel,sharedViewModel: SharedViewModel, account: GoogleSignInAccount?) {
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(true){
        while (true) {
            if (!isInternetAvailable(context)) {
                navController.navigate("nointernet")
            }
            delay(5000)
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeActivity(navController, boardDataViewModel, favoriteViewModel, sharedViewModel)
        }
        composable("nointernet") {
            NoInternetScreen(navController)
        }
        composable("search") {
            searchActivity(navController, boardSearchViewModel, sharedViewModel)
        }
        composable("favorite") {
            FavoriteActivity(navController, favoriteViewModel, sharedViewModel)
        }
        composable("personal"){
            PersonalActivity(navController)
        }

        composable(
            route = "boardgameinfo/{gameID}",
            arguments = listOf(navArgument("gameID") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val gameID = arguments.getString("gameID")
            SimpleBoardGameInfoActivity(
                navController,
                gameID,
                boardDataViewModel,
                ratingsViewModel,
                favoriteViewModel,
                sharedViewModel
            )
        }
        composable(
            route = "complexBoardgameinfo/{gameID}",
            arguments = listOf(navArgument("gameID") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val gameID = arguments.getString("gameID")
            ComplexBoardGameInfoActivity(
                navController,
                gameID,
                boardDataViewModel,
                ratingsViewModel,
                favoriteViewModel,
                sharedViewModel
            )
        }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        if (sharedViewModel.previousTab != "complexBoardgameinfo/{gameID}"
            && currentRoute == "boardgameinfo/{gameID}"){
            sharedViewModel.goBackToElseThanInfo = sharedViewModel.previousTab
        }
        if (currentRoute != null && currentRoute != sharedViewModel.previousTab) {
            sharedViewModel.previousTab = currentRoute
        }
    }
}