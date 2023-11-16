package com.example.myapplication


import android.content.Intent
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
import com.example.myapplication.modelviews.MyViewModel
import com.example.myapplication.repositories.AuthenticationManager
import com.example.myapplication.views.LoginScreen
import com.example.myapplication.views.searchActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException


class MainActivity : ComponentActivity() {

    private lateinit var authManager: AuthenticationManager
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val viewModel: MyViewModel by viewModels()
        authManager = AuthenticationManager(this, viewModel)
        authManager.signOut()
        setContent {
            boardgameApp(viewModel, authManager, account)
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
fun boardgameApp(viewModel: MyViewModel, authManager: AuthenticationManager, account: GoogleSignInAccount?) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(viewModel, navController) { authManager.signIn() }
        }
        composable("home") {
            HomeActivity(navController, viewModel)
        }
        composable("search") {
            searchActivity(navController, viewModel)
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



