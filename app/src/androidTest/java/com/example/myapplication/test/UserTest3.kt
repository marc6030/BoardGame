package com.example.myapplication.test

import android.util.Log
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
            waitForResponse(viewModel.boardGameList)
            Assert.assertTrue(viewModel.boardGameList != null)
        } catch(e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @When ("the user selects a game from trending section")
    fun doWhen() {
        // should probably be a ui test
        Assert.assertTrue(true)
    }

    @Then ("the game details are obtained")
    fun doThen() {
        val animaGameSearchResult = viewModel.boardGameSearch!!.boardGameSearchItems.find{ it.name == "Anima" }
        try {
            viewModel.fetchBoardGameData(animaGameSearchResult!!.id)
            val start_time = System.currentTimeMillis()
            val timeout = start_time + 15000

            while (System.currentTimeMillis() < timeout && viewModel.boardGameData == null) {
                Thread.sleep(200)
            }
        } catch (e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
        Log.v("AnimaAge", viewModel.boardGameData!!.age)
        Assert.assertTrue(viewModel.boardGameData?.age == "4")
        Log.v("AnimaAge", viewModel.boardGameData!!.mechanisms.first())
        Assert.assertTrue(viewModel.boardGameData?.mechanisms!!.contains("Dice Rolling"))
        Log.v("AnimaAge", viewModel.boardGameData!!.ratingBGG)
        Assert.assertFalse(viewModel.boardGameData?.ratingBGG.isNullOrEmpty())
    }

    fun <T : Any> waitForResponse(valToChange: T?) {
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        while (System.currentTimeMillis() < timeout && valToChange == null) {
            Thread.sleep(200)
        }
    }
}
