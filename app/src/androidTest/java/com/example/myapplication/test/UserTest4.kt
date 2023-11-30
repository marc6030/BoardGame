package com.example.myapplication.test

import android.util.Log
import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest4 {
    private val viewModel = MyViewModel()

    @Given("The user is logged in")
    fun doGiven() {
        try {
            Assert.assertTrue(viewModel.userAuthenticated)
        } catch(e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @When ("the user adds a game to favorites")
    fun doWhen() {
        try {
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
        waitForResponse(viewModel.favoriteBoardGameList)
        Assert.assertTrue(viewModel.favoriteBoardGameList.any {it?.id == viewModel.boardGameList!!.boardGames.first().id})
    }

    fun <T : Any> waitForResponse(valToChange: T?) {
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && valToChange == null) {
            Thread.sleep(200)
        }
    }
}
