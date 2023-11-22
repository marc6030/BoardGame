package com.example.myapplication;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

class TestOne {
}

@RunWith(AndroidJUnit4::class)
class TitleScreenTest {

    @Test
    fun testNavigationToInGameScreen() {
        // Create a TestNavHostController
        val navController = TestNavHostController(
                ApplicationProvider.getApplicationContext())

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<TitleScreen>()

        titleScenario.onFragment { fragment ->
                // Set the graph on the TestNavHostController
                navController.setGraph(R.navigation.trivia)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.play_btn)).perform(ViewActions.click())
        assertThat(navController.currentDestination?.id).isEqualTo(R.id.in_game)
    }
}
