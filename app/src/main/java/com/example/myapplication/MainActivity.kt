package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardSearchViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


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
            boardgameApp(favoriteViewModel, ratingsViewModel, boardDataViewModel, boardSearchViewModel,
                viewModel, account)
        }
    }


}



@Composable
fun boardgameApp(favoriteViewModel: FavoriteViewModel, ratingsViewModel: RatingsViewModel, boardDataViewModel: BoardDataViewModel, boardSearchViewModel: BoardSearchViewModel,sharedViewModel: SharedViewModel, account: GoogleSignInAccount?) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeActivity(navController, boardDataViewModel, favoriteViewModel, sharedViewModel)
        }
        composable("search") {
            searchActivity(navController, boardSearchViewModel)
        }
        composable("favorite") {
            FavoriteActivity(navController, favoriteViewModel, sharedViewModel)
        }
        composable(
            route = "boardgameinfo/{gameID}",
            arguments = listOf(navArgument("gameID") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val gameID = arguments.getString("gameID")
            BoardGameInfoActivity(navController, gameID, boardDataViewModel, ratingsViewModel, favoriteViewModel, sharedViewModel)
        }
    }
}



