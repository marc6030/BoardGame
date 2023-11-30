package com.example.myapplication.test

import android.util.Log
import com.example.myapplication.modelviews.BoardDataViewModel
import com.example.myapplication.modelviews.BoardSearchViewModel
import com.example.myapplication.modelviews.RatingsViewModel
import com.example.myapplication.modelviews.SharedViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest1 {
    private val viewModel = SharedViewModel()
    private val ratingsViewModel = RatingsViewModel(viewModel)
    private val boardGameDataViewModel = BoardDataViewModel()
    private val boardGameSearchViewModel = BoardSearchViewModel()

    @Given("the board game search functionality is available")
    fun i_work() {
        println("NICE")

        try {
            boardGameDataViewModel.fetchBoardGameData("test")
            Assert.assertTrue(true)
        } catch (e: Exception) {
            Assert.assertTrue(false)
        }
    }

    @When("the user submits a search for \"Monopoly\"")
    fun sfds() {

        try{
            boardGameSearchViewModel.fetchGameBoardSearch("monopoly")
            Assert.assertTrue(true)
        } catch(e: Exception) {
            Assert.assertTrue(false)
        }
    }

    @Then("the search results should include \"Monopoly\" among the returned games")
    fun qq() {
        val start_time = System.currentTimeMillis()
        val timeout = start_time + 15000

        var searchResults = boardGameSearchViewModel.boardGameSearch?.boardGameSearchItems ?: emptyList()

        while (System.currentTimeMillis() < timeout && searchResults.isEmpty()) {
            Thread.sleep(200);
            searchResults = boardGameSearchViewModel.boardGameSearch?.boardGameSearchItems ?: emptyList();
        }

        Log.v("monopolyGame", "$searchResults")

        val monopolyGame = searchResults.any { it.name.contains("Monopoly", ignoreCase = true) }

        if (monopolyGame) {
            Assert.assertTrue(true)
        } else {
            Assert.assertTrue(false)
        }
    }
}
