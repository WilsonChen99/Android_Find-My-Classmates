package com.example.csci310_group15;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SendMessageTestEspresso {
    public static final String STRING_TO_BE_TYPED_EMAIL = "tester1@usc.edu";
    public static final String STRING_TO_BE_TYPED_PASSWORD = "000000";
    public static final String STRING_TO_BE_TYPED_MESSAGE = "Howdy";
    public static final String STRING_TO_BE_DISPLAYED = "Howdy";
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void sendMessage() {
        // Successfully login
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

        // Navigate to contacts list
        onView(withId(R.id.btnChat)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Enter chat conversation page
        onView(withId(R.id.recycle)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Send message
        onView(withId(R.id.message))
                .perform(typeText(STRING_TO_BE_TYPED_MESSAGE), closeSoftKeyboard());
        onView(withId(R.id.sendIcon)).perform(click());

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check that message is displayed on screen
        onView(withId(R.id.chatRecycle)).check(matches(hasDescendant(withText(STRING_TO_BE_DISPLAYED))));
    }
}
