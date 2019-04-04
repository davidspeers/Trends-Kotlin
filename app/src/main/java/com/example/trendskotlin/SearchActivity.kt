/*package com.example.trendskotlin

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

// 1
class SearchActivity : AppCompatActivity(), SearchPresenter.View {

    private val presenter: SearchPresenter = SearchPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // 2
        presenter.attachView(this)

        searchButton.setOnClickListener {
            val query = ingredients.text.toString()
            // 3
            presenter.search(query)
        }
    }

    override fun onDestroy() {
        // 4
        presenter.detachView()
        super.onDestroy()
    }

    // 5
    override fun showQueryRequiredMessage() {
        // Hide keyboard
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        Snackbar.make(searchButton, getString(R.string.search_query_required), Snackbar
            .LENGTH_LONG).show()
    }

    // 6
    override fun showSearchResults(query: String) {
        startActivity(searchResultsIntent(query))
    }
}*/