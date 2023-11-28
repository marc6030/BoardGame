package io.cucumber.skeleton

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.modelviews.MyViewModel
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class UserTest1 {
    @Given("the board game search functionality is available")
    fun i_work() {
        val viewModel: MyViewModel = MyViewModel()

    }


    @When("the user submits a search for \"Monopoly\"")
    fun sfds() {
        println("nej")
    }

    @Then("the search results should include \"Monopoly\" among the returned games")
    fun qq() {
        println("hej")
    }
}
