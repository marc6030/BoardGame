package not_cucumber

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.example.myapplication.MainActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class LoginTest {

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
            if (composeTestRule.activity.viewModel.userAuthenticated) {
                return  // Sign-in completed
            }
            // Sleep a bit to avoid constant polling
            Thread.sleep(500)
        }
        throw TimeoutException("Timed out waiting for user sign-in to complete")
    }

    @Test
    fun signIn() {
        Assert.assertTrue(composeTestRule.activity.viewModel.userAuthenticated)
    }

}





