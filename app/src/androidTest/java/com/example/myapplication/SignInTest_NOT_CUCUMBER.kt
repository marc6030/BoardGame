package com.example.myapplication

import android.util.Log
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class UserTest1 {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        // Perform initial setup tasks if needed

        composeTestRule.onNodeWithText("Sign In with Google").performClick()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val emailButton = device.findObject(UiSelector().text("emil.s.simonsen@gmail.com"))
        if (emailButton.exists() && emailButton.isEnabled) {
            emailButton.clickAndWaitForNewWindow()
        }

        // Wait for the UI to become idle or add appropriate synchronization logic
        waitForSignInToComplete()
    }

    // Implemented to ensure the user is actually logged in
    private fun waitForSignInToComplete() {
        val startTime = System.currentTimeMillis()
        val timeout = 20000  // 20 seconds timeout for sign in
        while (System.currentTimeMillis() - startTime < timeout) {
            if (composeTestRule.activity.viewModel.isUserLoggedInGoogle.value != null) {
                return  // Sign-in completed
            }
            // Sleep a bit to avoid constant polling
            Thread.sleep(500)
        }
        throw TimeoutException("Timed out waiting for user sign-in to complete")
    }

    private fun waitForBoardData() {
        val startTime = System.currentTimeMillis()
        val timeout = 20000  // 20 seconds timeout for sign in
        while (System.currentTimeMillis() - startTime < timeout) {
            if (composeTestRule.activity.viewModel.boardGameDataList.value != null) {
                return  // Sign-in completed
            }
            // Sleep a bit to avoid constant polling
            Thread.sleep(500)
        }
        throw TimeoutException("Timed out waiting for user sign-in to complete")
    }

    @Test
    fun signIn() {
        val isUserLoggedIn = composeTestRule.activity.viewModel.isUserLoggedInGoogle.value
        Log.v("TestSignIn", "${isUserLoggedIn}")
        Assert.assertTrue(isUserLoggedIn != null)
    }


    //@Test
    // @Given("I am on the HomeActivity")
    fun i_am_on_the_home_activity() {
        val boardID = composeTestRule.activity.viewModel.boardGameDataList.value?.boardGames?.first()
        val sss = composeTestRule.activity.viewModel.boardGameDataList.value
        Log.v("Test1", "${sss}")

        if (boardID != null) {
            Assert.assertTrue(!boardID.equals(""))
        }

    }

    // @When("I click on a board game picture")
    fun i_click_on_a_board_game_picture() {
        // Click on the picture. Replace with appropriate logic for Compose
        composeTestRule.onNodeWithTag("YourPictureTag").performClick()
        composeTestRule
            .onAllNodesWithTag("boardGameItem")
            .onFirst()
            .performScrollTo()
            .performClick()

        val gameHasID = composeTestRule.activity.viewModel.boardGameData.value?.id
        Assert.assertTrue(gameHasID != null)
    }

    // @Then("I should navigate to BoardGameInfoActivity")
    fun i_should_navigate_to_board_game_info_activity() {
        // Check if BoardGameInfoActivity is displayed or if the navigation occurred
    }
}

