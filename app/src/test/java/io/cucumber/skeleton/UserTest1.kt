package io.cucumber.skeleton

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When


class UserTest1 {
    @Given("The user searches for a board game")
    fun i_work() {
        println("im not crashing")
    }


    @When("The users enters his search")
    fun sfds() {
        println("nej")
    }

    @Then("The search results is returned")
    fun qq() {
        println("hej")
    }
}