package com.example.myapplication.test

import android.util.Log
import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest1 {


    private val viewModel = MyViewModel()

    @Given("the board game search functionality is available")
    fun i_work() {
        println("NICE")

        try {
            viewModel.fetchBoardGameData("test")
            Assert.assertTrue(true)
        } catch (e: Exception) {
            Assert.assertTrue(false)
        }


    }

    @When("the user submits a search for \"Monopoly\"")
    fun sfds() {

        try{
            viewModel.fetchGameBoardSearch("monopoly")
            Assert.assertTrue(true)
        } catch(e: Exception) {
            Assert.assertTrue(false)
        }


    }

    @Then("the search results should include \"Monopoly\" among the returned games")
    fun qq() {
        Thread.sleep(5000)
        val searchResults = viewModel.boardGameSearchResults.value?.boardGameSearchItems ?: emptyList()
        Log.v("monopolyGame", "$searchResults")

        val monopolyGame = searchResults.any { it.name.contains("Monopoly", ignoreCase = true) }

        if (monopolyGame) {
            Assert.assertTrue(true)
            // Element with title "Monopoly" found
            // You can perform further operations with `monopolyGame`
        } else {
            // No element with title "Monopoly" was found in the list
            Assert.assertTrue(false)
        }



    }
}
