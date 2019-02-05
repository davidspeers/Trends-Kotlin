package com.example.trendskotlin

class SearchPresenter {
    // 1
    private var view: View? = null

    // 2
    fun attachView(view: View) {
        this.view = view
    }

    // 3
    fun detachView() {
        this.view = null
    }

    // 4
    fun search(query: String) {
        // 5
        if (query.trim().isBlank()) {
            view?.showQueryRequiredMessage()
        } else {
            view?.showSearchResults(query)
        }
    }

    // 6
    interface View {
        fun showQueryRequiredMessage()
        fun showSearchResults(query: String)
    }
}