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

class UserTest5 {
    private val viewModel = MyViewModel()

    @Given("The user is logged in and has selected a game")
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
        viewModel.fetchBoardGameList()
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && viewModel.boardGameList == null) {
            Thread.sleep(200)
        }
        Assert.assertTrue(viewModel.userAuthenticated)
    }

    @When ("the user adds a rating to a game")
    fun doWhen() {
        viewModel.insertAverageRating(viewModel.boardGameList!!.boardGames.first().id, "5")
        Assert.assertTrue(true)
    }

    @Then ("the users rating for that game should change")
    fun doThen() {
        viewModel.fetchBoardGameData(viewModel.boardGameList!!.boardGames.first().id)
        var start_time = System.currentTimeMillis()
        var timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && viewModel.boardGameData == null) {
            Thread.sleep(200)
        }

        viewModel.userRating = "testing"
        viewModel.fetchUserRating(viewModel.boardGameData!!)
        start_time = System.currentTimeMillis()
        timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && viewModel.userRating == "testing") {
            Thread.sleep(200)
        }

        Assert.assertTrue(viewModel.userRating == "5")
    }
}
