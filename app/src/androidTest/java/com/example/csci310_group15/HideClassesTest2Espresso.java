package com.example.csci310_group15;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HideClassesTest2Espresso {
    public static final String STRING_TO_BE_TYPED_EMAIL = "tester1@usc.edu";
    public static final String STRING_TO_BE_TYPED_PASSWORD = "000000";

    public static final String DEPARTMENT_TAB = "Aerospace and Mechanical Engineering";
    public static final String CLASS_TAB_201 = "AME-201";
    public static final String CLASS_TAB_204 = "AME-204";
    public static final String CLASS_TAB_261 = "AME-261";
    public static final String CLASS_TAB_302 = "AME-302";

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void revealClasses() {
        // Successfully login
        onView(withId(R.id.email))
                .perform(typeText(STRING_TO_BE_TYPED_EMAIL), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(STRING_TO_BE_TYPED_PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.loginBtn)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Switch to main page
        onView(withId(R.id.btnHome)).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reveal class tabs
        onView(withText(DEPARTMENT_TAB)).perform((click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Hide class tabs
        onView(withText(DEPARTMENT_TAB)).perform((click()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withText(CLASS_TAB_201)).check(matches(not(isDisplayed())));
        onView(withText(CLASS_TAB_204)).check(matches(not(isDisplayed())));
        onView(withText(CLASS_TAB_261)).check(matches(not(isDisplayed())));
        onView(withText(CLASS_TAB_302)).check(matches(not(isDisplayed())));
    }
}
