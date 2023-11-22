package com.example.myapplication;

import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.cucumber.java.en.Given;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestOne {


    @Given("The user is on the search page")
    public void i_work() {
        TestNavHostController testNav = new TestNavHostController(ApplicationProvider.getApplicationContext());
        testNav.setCurrentDestination(1);
    }

}

