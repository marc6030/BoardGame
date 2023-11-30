package com.example.myapplication.test

import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest3 {
    private val viewModel = MyViewModel()

    @Given("the user is on the front page")
    fun doGiven() {
        try {
            viewModel.fetchBoardGameList()
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 15000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameList == null) {
                Thread.sleep(200)
            }
            Assert.assertTrue(viewModel.boardGameList != null)
        } catch(e: Exception) {
            e.printStackTrace()
            Assert.assertTrue(false)
        }
    }

    @When ("the user selects a game from trending section")
    fun doWhen() {
        // should probably be a ui test
        Assert.assertTrue(true)
    }

    @Then ("the game details are obtained")
    fun doThen() {
        viewModel.fetchBoardGameData(viewModel.boardGameList!!.boardGames.first().id)
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && viewModel.boardGameData == null) {
            Thread.sleep(200)
        }

        Assert.assertTrue(viewModel.boardGameData?.age != null)
        Assert.assertTrue(viewModel.boardGameData?.mechanisms != null)
        Assert.assertTrue(viewModel.boardGameData?.ratingBGG != null)
    }

}
