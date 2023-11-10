package com.example.csci310_group15;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FailureSignUpTestEspresso {
    public static final String STRING_TO_BE_TYPED_EMAIL = "fish@usc.edu";
    public static final String STRING_TO_BE_TYPED_NAME = "Nemo";
    public static final String STRING_TO_BE_TYPED_ID = "0000";
    public static final String STRING_TO_BE_TYPED_ROLE = "undergrad";
    public static final String STRING_TO_BE_TYPED_PASSWORD = "123456";
    public static final String STRING_TO_BE_DISPLAYED = "USC Email";

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void failureSignUp() {
        // Go to Sign Up page
        onView(withId(R.id.signUpBtn)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // User fails to enter all necessary credentials
        onView(withId(R.id.email))
                .perform(typeText(STRING_TO_BE_TYPED_EMAIL), closeSoftKeyboard());

        onView(withId(R.id.name))
                .perform(typeText(STRING_TO_BE_TYPED_NAME), closeSoftKeyboard());

        onView(withId(R.id.ID))
                .perform(typeText(STRING_TO_BE_TYPED_ID), closeSoftKeyboard());

        onView(withId(R.id.role))
                .perform(typeText(STRING_TO_BE_TYPED_ROLE), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(STRING_TO_BE_TYPED_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that we are still in Sign Up page
        onView(withId(R.id.emailLabel)).check(matches(withText(STRING_TO_BE_DISPLAYED)));
    }
}
