package com.example.myapplication.test

import com.example.myapplication.modelviews.MyViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert

class UserTest4 {
    private val viewModel = MyViewModel()

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
            viewModel.fetchBoardGameList()
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 15000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameList == null) {
                Thread.sleep(200)
            }

            viewModel.insertIntoUserFavoriteDB(viewModel.boardGameList!!.boardGames.first().id)
            Assert.assertTrue(true)
        } catch (e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @Then ("the game should appear in the users favorites list")
    fun doThen() {
        viewModel.fetchFavoriteListFromDB()
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && viewModel.favoriteBoardGameList.isEmpty()) {
            Thread.sleep(200)
        }
        Assert.assertTrue(viewModel.favoriteBoardGameList.any {it?.id == viewModel.boardGameList!!.boardGames.first().id})
    }
}
