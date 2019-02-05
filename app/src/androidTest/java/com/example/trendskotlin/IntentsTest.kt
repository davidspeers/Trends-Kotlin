package com.example.trendskotlin

import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

class IntentsTest {

    @get:Rule
    var intentsTestRule = IntentsTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun testIntents() {
        onView(withId(R.id.partyAddBtn)).perform(click())
        onView(withId(R.id.partyPlayBtn)).perform(click())

        //validate intent and check its data
        intended(
            allOf(
                toPackage("com.example.trendskotlin"),
                hasExtra("mode", "Party Mode"),
                hasExtra("secondaryChoice", "3")
            )
        )
    }
}