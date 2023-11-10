package com.example.csci310_group15;

import android.app.Activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SuccessLoginTestEspresso {
    public static final String STRING_TO_BE_TYPED_EMAIL = "tester1@usc.edu";
    public static final String STRING_TO_BE_TYPED_PASSWORD = "000000";
    public static final String STRING_TO_BE_DISPLAYED = "HOME";
    @Rule public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void successLogin() {
        // Enter email and password of registered user
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

        // Check that we successfully navigated to new page
        onView(withId(R.id.btnHome)).check(matches(withText(STRING_TO_BE_DISPLAYED)));
    }
}
