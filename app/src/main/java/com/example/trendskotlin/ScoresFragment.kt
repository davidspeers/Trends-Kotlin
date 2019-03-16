package com.example.trendskotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ScoresFragment : Fragment() {

    operator fun JSONArray.iterator(): Iterator<Any> = (0 until length()).asSequence().map { get(it) }.iterator()

    fun showResults(linearLayout: LinearLayout, results: JSONArray) {
        results.iterator().forEach {
            val childLayout = LinearLayout(context)
            linearLayout.addView(childLayout)
            childLayout.apply {
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 10.0f
                ).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    addView(TextView(context).apply {
                        weight = 1.0f
                        text = it.toString()
                        gravity = Gravity.CENTER
                    })
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(com.example.trendskotlin.R.layout.fragment_scores, container, false)
        val linearLayout = rootView.findViewById<LinearLayout>(com.example.trendskotlin.R.id.linearLayout)

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(activity)
        val url = "https://trends-app-server.herokuapp.com/"

        val jsonObj = JSONObject()
        jsonObj.put("secret_val", "potato")
        jsonObj.put("mode", "Party Mode")
        jsonObj.put("query", arguments?.getString(QUERY))
        jsonObj.put("values", JSONArray(arguments?.getStringArray(TERMS)))

        //Log.e("sending json", params.toString())
        var allValues: MutableList<JSONArray> = mutableListOf()
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST, url,
            jsonObj, Response.Listener<JSONObject> { response ->
                //Log.i("Potatoes", response.toString())
                val avgs = response.optJSONArray("averages")
                activity!!.applicationContext.getString(com.example.trendskotlin.R.string.scores, "Picnic Basket", avgs)
                showResults(linearLayout, avgs)
                Log.d("Entire JSON", response.toString())
                Log.d("Averages", response.optJSONArray("averages").toString())
                response.getJSONArray("values").iterator().forEach {
                    Log.d("Scores", JSONObject(it.toString()).getJSONArray("value").toString())
                    allValues.add(JSONObject(it.toString()).getJSONArray("value"))
                }
                rootView.findViewById<ProgressBar>(com.example.trendskotlin.R.id.progressbar).visibility = View.GONE
                val SM = activity as SendMessage
                SM.sendData(allValues.toString())
            },
            Response.ErrorListener { error ->
                val textView = TextView(context)
                textView.text = "Error - " + error.toString() + ' ' + error.networkResponse.statusCode.toString()
                linearLayout.addView(
                    textView
                )
                rootView.findViewById<ProgressBar>(com.example.trendskotlin.R.id.progressbar).visibility = View.GONE
            }
        )

        //values
        //cpuAnswer

        queue.add(jsonObjReq)

        /*// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
            Response.ErrorListener { textView.text = "That didn't work!" }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)*/

        //rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
        return rootView
    }

    internal interface SendMessage {
        fun sendData(message: String)
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
        fun newInstance(query: String, terms: Array<String>): ScoresFragment {
            val fragment = ScoresFragment()
            val args = Bundle()
            args.putString(QUERY, query)
            args.putStringArray(TERMS, terms)
            fragment.arguments = args
            return fragment
        }
    }
}