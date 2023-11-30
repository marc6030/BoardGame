package com.example.myapplication.test

import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.FavoriteViewModel
import com.example.myapplication.modelviews.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert


class UserTest4 {
    private val viewModel = SharedViewModel()
    private val boardGameDataViewModel = BoardDataViewModel()
    private val favoriteViewModel = FavoriteViewModel(viewModel)

    @Given("The user is logged in and is on a game page")
    fun doGiven() {
        runBlocking {
            val email = "emil.s.simonsen@gmail.com"
            val password = "123456"
            // Await the completion of Firebase authentication
            val task = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                viewModel.setUser(user)
                viewModel.setDB(FirebaseFirestore.getInstance())
                viewModel.userAuthenticated = true
            }

        }
        Assert.assertTrue(viewModel.userAuthenticated)
    }

    @When ("the user adds a game to favorites")
    fun doWhen() {
        try {
            boardGameDataViewModel.fetchBoardGameList()
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 25000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameList == null) {
                Thread.sleep(200)
            }

            favoriteViewModel.insertIntoUserFavoriteDB(viewModel.boardGameList!!.boardGames.first().id)
            Assert.assertTrue(true)
        } catch (e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @Then ("the game should appear in the users favorites list")
    fun doThen() {
        favoriteViewModel.fetchFavoriteListFromDB()
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 25000

        while (System.currentTimeMillis() < timeout && favoriteViewModel.favoriteBoardGameList.isEmpty()) {
            Thread.sleep(200)
        }
        Assert.assertTrue(favoriteViewModel.favoriteBoardGameList.any {it?.id == viewModel.boardGameList!!.boardGames.first().id})
    }
}
