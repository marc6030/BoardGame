package io.cucumber.skeleton

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class StepDefinitions {
    @Given("The user is on the search page")
    fun i_work() {
        println("im not crashing")
    }


    @When("The user enters \"monopoly\" into the search bar")
    fun sfds() {
        println("nej")
    }

    @Then("The user should be able to find all monopoly games")
    fun qq() {
        println("hej")
    }
}

