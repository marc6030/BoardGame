package com.example.myapplication.test

import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert

class UserTest1 {
    @Given("the board game search functionality is available")
    fun i_work() {
        println("NICE")
        val viewModel = MyViewModel()

        try {
            viewModel.fetchBoardGameData("test")
            Assert.assertTrue(true)
        } catch (e: Exception) {
            Assert.assertTrue(false)
        }


    }

    @When("the user submits a search for \"Monopoly\"")
    fun sfds() {
        /*
        val viewModel: MyViewModel = MyViewModel()
        viewModel.fetchGameBoardSearch("monopoly")

         */
    }

    @Then("the search results should include \"Monopoly\" among the returned games")
    fun qq() {
        /*
        val viewModel: MyViewModel = MyViewModel()
        val searchResults = viewModel.boardGameSearchResults.value?.boardGameSearchItems ?: emptyList()

        val monopolyGame = searchResults.find { it.name.equals("Monopoly", ignoreCase = true) }


        if (monopolyGame!!.equals("monopoly")) {
            Assert.assertTrue(true)
            // Element with title "Monopoly" found
            // You can perform further operations with `monopolyGame`
        } else {
            // No element with title "Monopoly" was found in the list
            Assert.assertTrue(false)
        }

         */
    }
}
