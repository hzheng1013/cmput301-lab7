package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario =
            new ActivityScenarioRule<>(MainActivity.class);

    private void addCity(String city) {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(ViewActions.typeText(city));
        onView(withId(R.id.button_confirm)).perform(click());
    }

    private void clickCityAtPosition(int position) {
        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(position)
                .perform(click());
    }

    // =========================
    // Original Tests
    // =========================

    @Test
    public void testAddCity() {
        addCity("Edmonton");
        onView(withText("Edmonton")).check(matches(isDisplayed()));
    }

    @Test
    public void testClearCity() {
        addCity("Edmonton");
        addCity("Vancouver");

        onView(withId(R.id.button_clear)).perform(click());

        onView(withText("Edmonton")).check(doesNotExist());
        onView(withText("Vancouver")).check(doesNotExist());
    }

    @Test
    public void testListView() {
        addCity("Edmonton");

        onData(is(instanceOf(String.class)))
                .inAdapterView(withId(R.id.city_list))
                .atPosition(0)
                .check(matches(withText("Edmonton")));
    }

    // =========================
    // ShowActivity Tests
    // =========================

    // 1) Check whether the activity correctly switched
    @Test
    public void testActivitySwitchedToShowActivity() {
        addCity("Edmonton");
        clickCityAtPosition(0);

        onView(withId(R.id.text_city_name)).check(matches(isDisplayed()));
        onView(withId(R.id.button_back)).check(matches(isDisplayed()));
    }

    // 2) Test whether the city name is consistent
    @Test
    public void testCityNameConsistent() {
        addCity("Vancouver");
        clickCityAtPosition(0);

        onView(withId(R.id.text_city_name)).check(matches(withText("Vancouver")));
    }

    // 3) Test the "back" button
    @Test
    public void testBackButtonReturnsToMainActivity() {
        addCity("Tokyo");
        clickCityAtPosition(0);

        onView(withId(R.id.button_back)).perform(click());

        onView(withId(R.id.button_add)).check(matches(isDisplayed()));
        onView(withId(R.id.city_list)).check(matches(isDisplayed()));
    }
}