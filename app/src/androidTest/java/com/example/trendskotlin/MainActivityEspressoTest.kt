package com.example.trendskotlin

import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click

import org.hamcrest.core.StringContains.containsString
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not
import java.lang.Exception


class MainActivityEspressoTest {

    @get:Rule
    val mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun checkAllViewsDisplayed() {
        onView(withId(R.id.action_bar))
            .check(matches(not(isDisplayed())))
        listOf(
            R.id.trendsLogo,
            R.id.helpBtn,
            R.id.PartyCard,
            R.id.partyTextView,
            R.id.partyAddBtn,
            R.id.partyRemBtn,
            R.id.partyPlayBtn,
            R.id.CpuCard,
            R.id.cpuTextView,
            R.id.cpuAddBtn,
            R.id.cpuRemBtn,
            R.id.cpuPlayBtn,
            R.id.achieveBtn
        ).forEach {
            viewDisplayed(it)
        }
    }

    @Test
    fun testAddAndRemButton() {
        //Note that it if an error occurs it returns the entire TextView - Commnad-f text to find the actual text
        onView(withId(R.id.partyTextView))
            .check(matches(withText("2")))
        clickButton(R.id.partyRemBtn)
        onView(withId(R.id.partyTextView))
            .check(matches(withText("2")))
        clickButton(R.id.partyAddBtn)
        onView(withId(R.id.partyTextView))
            .check(matches(withText("3")))
        clickButton(R.id.partyAddBtn, 3)
        onView(withId(R.id.partyTextView))
            .check(matches(withText("5")))
        clickButton(R.id.partyRemBtn)
        onView(withId(R.id.partyTextView))
            .check(matches(withText("4")))
    }

    @Test
    fun playButtons() {

    }

    fun viewDisplayed(rid: Int) {
        onView(withId(rid))
            .check(matches(isDisplayed()))
    }

    fun clickButton(rid: Int, xTimes: Int = 1) {
        when(xTimes) {
            1 -> onView(withId(rid)).perform(click())
            2 -> onView(withId(rid)).perform(click()).perform(click())
            3 -> onView(withId(rid)).perform(click()).perform(click()).perform(click())
            else -> throw Exception("Clicking button an unexpected number of times")
        }
    }

}