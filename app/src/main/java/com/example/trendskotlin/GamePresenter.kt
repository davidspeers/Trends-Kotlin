package com.example.trendskotlin

class GamePresenter {
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
    fun enterTerm(term: String) {
        // 5
        if (term.trim().isBlank()) {
            view?.showTermRequiredMessage()
        } else {
            view?.pushTerm(term)
        }
    }

    // 6
    interface View {
        fun showTermRequiredMessage()
        fun pushTerm(term: String)
    }
}