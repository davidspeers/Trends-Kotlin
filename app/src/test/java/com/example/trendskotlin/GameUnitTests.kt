package com.example.trendskotlin

import android.content.Context
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class GameUnitTests {
    // 1

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun search_withEmptyTerm_callsShowTermRequiredMessage() {

        val presenter = GamePresenter()

        val view= mock(GamePresenter.View::class.java)

        presenter.attachView(view)

        // 4
        presenter.enterTerm("")

        // 5
        verify(view).showTermRequiredMessage()
    }

    @Test
    fun search_withTerm_callsPushTerm() {

        val presenter = GamePresenter()

        val view= mock(GamePresenter.View::class.java)

        presenter.attachView(view)

        // 4
        presenter.enterTerm("A Term")

        // 5
        verify(view).pushTerm("A Term")
    }
}