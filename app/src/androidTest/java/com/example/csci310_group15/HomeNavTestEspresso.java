package com.example.csci310_group15;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeNavTestEspresso {
    public static final String STRING_TO_BE_TYPED_EMAIL = "tester1@usc.edu";
    public static final String STRING_TO_BE_TYPED_PASSWORD = "000000";
    public static final String STRING_TO_BE_DISPLAYED = "Computer Science";
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void homeNav() {
        // Enter email and password of user that does exist
        onView(withId(R.id.email))
                .perform(typeText(STRING_TO_BE_TYPED_EMAIL), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(STRING_TO_BE_TYPED_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Navigate to home page
        onView(withId(R.id.btnHome)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that we are in the home page
        onView(withId(R.id.mainPage)).check(matches(hasDescendant(withText(STRING_TO_BE_DISPLAYED))));
    }
}
