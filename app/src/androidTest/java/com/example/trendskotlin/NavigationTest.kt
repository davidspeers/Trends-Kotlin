package com.example.trendskotlin


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun gameTest() {
        val appCompatImageButton = onView(
            allOf(
                withId(R.id.partyAddBtn),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.partyPlayBtn),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val appCompatButton = onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.grid_view_themes),
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    )
                )
            )
            .atPosition(5)
        appCompatButton.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.editText),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("Bomb"), closeSoftKeyboard())

        val floatingActionButton = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.editText),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(replaceText("Compound"), closeSoftKeyboard())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.editText),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("Band"), closeSoftKeyboard())

        val floatingActionButton3 = onView(
            allOf(
                withId(R.id.fab),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton3.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
