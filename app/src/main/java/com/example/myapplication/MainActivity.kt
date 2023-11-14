package com.example.myapplication



import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.example.myapplication.modelviews.MyViewModel
import com.example.myapplication.repositories.AuthenticationManager
import com.example.myapplication.views.LoginScreen
import com.example.myapplication.views.SearchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthenticationManager

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthenticationManager(this)


        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            setContent {

                val viewModel: MyViewModel by viewModels()
                // lazy instantiation of views
                boardgameApp(viewModel)
            }
        } else {
            setContent {
                // Display login screen
                LoginScreen { authManager.signIn() }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AuthenticationManager.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            authManager.handleSignInResult(task)
            setContent{
                val viewModel: MyViewModel by viewModels()
                boardgameApp(viewModel)
            }
        }
    }
}




@Composable
fun boardgameApp(viewModel: MyViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeActivity(navController, viewModel)
        }
        composable("search") {
            SearchActivity(navController, viewModel)
        }
        composable("favorite") {
            FavoriteActivity(navController, viewModel)
        }
        composable(
            route = "boardgameinfo/{gameID}",
            arguments = listOf(navArgument("gameID") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val gameID = arguments.getString("gameID")
            BoardGameInfoActivity(navController, gameID, viewModel)
        }
    }
}



