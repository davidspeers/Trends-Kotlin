package com.example.trendskotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import java.util.*

class TrendsGraphFragment : Fragment() {

    var allValues = ""
    var query : String? = null
    var terms : String? = null

    fun displayReceivedData(message: String) {
        Log.d("message", message)
        allValues = message
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        query = arguments?.getString(TrendsGraphFragment.QUERY)
        terms = arguments?.getString(TrendsGraphFragment.TERMS)

        val rootView = inflater.inflate(R.layout.fragment_trends_graph, container, false)
        val trendsWebView = rootView.findViewById<WebView>(R.id.TrendsWebView)

        //trendsWebView.loadUrl("https://trends.google.com/trends/explore?geo=US&q=one,two")

        trendsWebView.addJavascriptInterface(WebAppInterface(), "Android")
        trendsWebView.settings.javaScriptEnabled = true
        trendsWebView.loadUrl("file:///android_asset/graph.html")

        return rootView
    }

    inner class WebAppInterface {

        @JavascriptInterface
        fun getQuery() : String {
            Log.d("Query", query)
            return query.toString()
        }

        @JavascriptInterface
        fun getTerms() : String {
            Log.d("Terms", terms)
            return terms.toString()
        }

        @JavascriptInterface
        fun getVals() : String {
            return allValues
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        //private val ARG_SECTION_NUMBER = "section_number"
        private val QUERY = "query"
        private val TERMS = "terms"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(query: String, terms: Array<String>): TrendsGraphFragment {
            val fragment = TrendsGraphFragment()
            val args = Bundle()
            args.putString(QUERY, query)
            args.putString(TERMS, Arrays.toString(terms))
            fragment.arguments = args
            return fragment
        }
    }
}