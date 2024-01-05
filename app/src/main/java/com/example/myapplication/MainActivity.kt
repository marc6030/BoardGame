package com.example.myapplication


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.example.myapplication.repositories.AuthenticationManager
import com.example.myapplication.views.LoginScreen
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException


class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthenticationManager
    val viewModel: SharedViewModel by viewModels()

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        authManager = AuthenticationManager(this)
        val ratingsViewModel = RatingsViewModel(viewModel)
        val boardDataViewModel = BoardDataViewModel(viewModel)
        val boardSearchViewModel = BoardSearchViewModel(viewModel)
        val favoriteViewModel = FavoriteViewModel(viewModel)

        setContent {
            AppTheme() {
                boardgameApp(favoriteViewModel, ratingsViewModel, boardDataViewModel, boardSearchViewModel,
                    viewModel, authManager, account)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AuthenticationManager.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                authManager.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Handle the error here
            }
        }
    }
}



@Composable
fun boardgameApp(favoriteViewModel: FavoriteViewModel, ratingsViewModel: RatingsViewModel, boardDataViewModel: BoardDataViewModel, boardSearchViewModel: BoardSearchViewModel,sharedViewModel: SharedViewModel, authManager: AuthenticationManager, account: GoogleSignInAccount?) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("login") {
            LoginScreen(sharedViewModel, navController) { authManager.signIn(sharedViewModel) }
        }
        composable("home") {
            HomeActivity(navController, boardDataViewModel, favoriteViewModel, sharedViewModel)
        }
        composable("search") {
            searchActivity(navController, boardSearchViewModel, sharedViewModel)
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



