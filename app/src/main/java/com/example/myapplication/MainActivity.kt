package com.example.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
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

    val transitionDuration = 2000 // ms
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
        startDestination = "personal"

    ) {
        composable("nointernet") {
            NoInternetScreen(navController)
        }
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
            FavoriteActivity(navController, favoriteViewModel, boardGameInfoActivity)
        }
        composable(
            route = "category/{category}",
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
                navBackStackEntry ->
            val category = navBackStackEntry.arguments?.getString("category")!!
            CategoryActivity(
                navController = navController,
                viewModel = boardDataViewModel,
                boardGameInfoActivity = boardGameInfoActivity ,
                category = category,
                row = 1
                //SHOULD BE MODIFIED TO WORK PROPERLY
            )
        }
        composable(
            route = "challenge",
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
            ChallengeActivity(navController, boardDataViewModel, boardGameInfoActivity)
        }
        composable(
            route = "playedGames",
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
            PlayedGamesActivity(navController, playedGamesViewModel, boardGameInfoActivity)
        }
        composable(
            route = "ratedGames",
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
            RatedGamesActivity(navController, ratingsViewModel, boardGameInfoActivity)
        }
        composable(
            route = "personal"
        ) {
            PersonalActivity(navController, viewModel = boardDataViewModel)
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