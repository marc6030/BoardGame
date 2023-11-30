package com.example.myapplication.test

import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.SharedViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest3 {
    private val viewModel = SharedViewModel()
    private val boardGameDataViewModel = BoardDataViewModel()


    @Given("the user is on the front page")
    fun doGiven() {
        try {
            boardGameDataViewModel.fetchBoardGameList()
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 25000

            while (System.currentTimeMillis() < timeout && boardGameDataViewModel.boardGameList == null) {
                Thread.sleep(200)
            }
            Assert.assertTrue(boardGameDataViewModel.boardGameList != null)
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
        boardGameDataViewModel.fetchBoardGameData(boardGameDataViewModel.boardGameList!!.boardGames.first().id)
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 25000

        while (System.currentTimeMillis() < timeout && boardGameDataViewModel.boardGameData == null) {
            Thread.sleep(200)
        }

        Assert.assertTrue(boardGameDataViewModel.boardGameData?.age != null)
        Assert.assertTrue(boardGameDataViewModel.boardGameData?.mechanisms != null)
        Assert.assertTrue(boardGameDataViewModel.boardGameData?.ratingBGG != null)
    }

}
