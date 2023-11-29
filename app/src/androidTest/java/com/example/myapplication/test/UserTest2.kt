package com.example.myapplication.test

import android.util.Log
import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest2 {
    private val viewModel = MyViewModel()

    @Given("the game is in the search results")
    fun doGiven() {
        try {
            viewModel.fetchGameBoardSearch("Anima")
            Thread.sleep(5000)
            val searchResults = viewModel.boardGameSearch?.boardGameSearchItems
            val isAnimaPresent = searchResults?.any { it.name.contains("Anima", ignoreCase = true)}
            Assert.assertTrue(isAnimaPresent != null && isAnimaPresent)
        } catch(e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
    }

    @When ("the user selects Anima")
    fun doWhen() {
        // should probably be a ui test
        Assert.assertTrue(true)
    }

    @Then ("the details of the game should be accessible")
    fun doThen() {
        val animaGameSearchResult = viewModel.boardGameSearch!!.boardGameSearchItems.find{ it.name == "Anima" }
        try {
            viewModel.fetchBoardGameData(animaGameSearchResult!!.id)
        } catch (e: Exception) {
            Assert.assertTrue(false)
            e.printStackTrace()
        }
        Log.v("AnimaAge", viewModel.boardGameData!!.age)
        Assert.assertTrue(viewModel.boardGameData?.age == "4+")
        Log.v("AnimaAge", viewModel.boardGameData!!.mechanisms.first())
        Assert.assertTrue(viewModel.boardGameData?.mechanisms!!.contains("Dice Rolling"))
        Log.v("AnimaAge", viewModel.boardGameData!!.age)
        Assert.assertFalse(viewModel.boardGameData?.ratingBGG.isNullOrEmpty())
    }
}
