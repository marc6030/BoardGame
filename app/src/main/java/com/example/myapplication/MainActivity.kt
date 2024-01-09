package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardGameInfoActivity
import com.example.myapplication.modelviews.BoardSearchViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.example.myapplication.views.PersonalActivity
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


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
        val favoriteViewModel = FavoriteViewModel()
        val boardGameInfoActivity = BoardGameInfoActivity()

        setContent {
            navController = rememberNavController()
            AppTheme(useDarkTheme = true) {
                boardgameApp(favoriteViewModel, ratingsViewModel, boardDataViewModel, boardSearchViewModel,
                    viewModel, account, navController, boardGameInfoActivity)
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
                 boardSearchViewModel: BoardSearchViewModel,sharedViewModel: SharedViewModel, account: GoogleSignInAccount?, navController: NavHostController,
                 boardGameInfoActivity: BoardGameInfoActivity) {

    val transitionDuration = 2000 // ms
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(
            route = "home",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            })
        {
            HomeActivity(navController, boardDataViewModel)
        }
        composable(
            route = "search",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            }) {

            searchActivity(navController, boardSearchViewModel)


        }
        composable(
            route = "favorite",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(transitionDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(transitionDuration)
                )
            }) {
            FavoriteActivity(navController, favoriteViewModel, sharedViewModel)
        }
        composable(
            route = "personal"
        ) {
            PersonalActivity(navController)
        }

        composable(
            route = "boardgameinfo/{gameID}",
            enterTransition = {
                scaleIn(
                    animationSpec = tween(transitionDuration)
                )
            },
            exitTransition = {
                scaleOut(
                    animationSpec = tween(transitionDuration)
                )
            },
            popEnterTransition = {
                scaleIn(
                    animationSpec = tween(transitionDuration)
                )
            },
            popExitTransition = {
                scaleOut(
                    animationSpec = tween(transitionDuration)
                )
            }
        )
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