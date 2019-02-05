package com.example.trendskotlin

import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.fragment_results.view.*
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.view.View.generateViewId
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.fragment_scores.*
import org.json.JSONArray


class ResultsActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_results, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return when (position) {
                0 -> ScoresFragment.newInstance(intent.getStringExtra("query"), intent.getStringArrayExtra("terms"))
                1 -> TrendsGraphFragment.newInstance(intent.getStringExtra("query"), intent.getStringArrayExtra("terms"))
                else -> throw Exception("Number of Pages out of range")
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_results, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * Results Fragment
     */
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
            val rootView = inflater.inflate(R.layout.fragment_scores, container, false)
            val linearLayout = rootView.findViewById<LinearLayout>(R.id.linearLayout)

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(activity)
            val url = "https://trends-app-server.herokuapp.com/"

            val jsonObj = JSONObject()
            jsonObj.put("secret_val", "potato")
            jsonObj.put("mode", "Party Mode")
            jsonObj.put("query", arguments?.getString(QUERY))
            jsonObj.put("values", JSONArray(arguments?.getStringArray(TERMS)))

            //Log.e("sending json", params.toString())
            val jsonObjReq = JsonObjectRequest(
                Request.Method.POST, url,
                jsonObj, Response.Listener<JSONObject> { response ->
                    //Log.i("Potatoes", response.toString())
                    val avgs = response.optJSONArray("averages")
                    activity!!.applicationContext.getString(R.string.scores, "Picnic Basket", avgs)
                    showResults(linearLayout, avgs)
                    rootView.findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
                },
                Response.ErrorListener { error ->
                    val textView = TextView(context)
                    textView.text = "Error - " + error.toString() + ' ' + error.networkResponse.statusCode.toString()
                    linearLayout.addView(
                        textView
                    )
                    rootView.findViewById<ProgressBar>(R.id.progressbar).visibility = View.GONE
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

    class TrendsGraphFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val textArray = arrayOf("One", "Two", "Three", "Four")

            val rootView = inflater.inflate(R.layout.fragment_trends_graph, container, false)
            val trendsWebView = rootView.findViewById<WebView>(R.id.TrendsWebView)

            //trendsWebView.loadUrl("https://trends.google.com/trends/explore?geo=US&q=one,two")

            trendsWebView.addJavascriptInterface(WebAppInterface(), "Android");
            trendsWebView.getSettings().setJavaScriptEnabled(true);
            trendsWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            trendsWebView.getSettings().setLoadWithOverviewMode(true);
            trendsWebView.loadUrl("file:///android_asset/graph.html")

            return rootView
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
                args.putStringArray(TERMS, terms)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /*fun youFunctionForVolleyRequest(
        email: String,
        password: String,
        context: Context,
        URL: String,
    ) {
        val params = HashMap<String, String>()
        params["email"] = email
        params["password"] = password

        Log.e("sending json", params.toString())
        val jsonObjReq = object : JsonObjectRequest(Request.Method.POST,
            URL, JSONObject(params), Response.Listener { response ->
                callback.onSuccess(response) // call call back function here
            }, Response.ErrorListener {
                //VolleyLog.d("Volley error json object ", "Error: " + error.getMessage());
            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
        }

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq)

    }*/
}

class WebAppInterface {

        @JavascriptInterface
        fun getNum1() : Int {
            return 1
        }

        @JavascriptInterface
        fun getNum2() : Int {
            return 2
        }

        @JavascriptInterface
        fun getNum3() : Int {
            return 3
        }

        @JavascriptInterface
        fun getNum4() : Int {
            return 4
        }

        @JavascriptInterface
        fun getNum5() : Int {
            return 5
        }
    }

class someTask() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        // ...
        return "hello"
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}
