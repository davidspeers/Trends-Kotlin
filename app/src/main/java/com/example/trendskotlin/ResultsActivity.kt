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


class ResultsActivity : AppCompatActivity(), ScoresFragment.SendMessage {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    //I believe this (and the ScoresFragment.SendMessage extension that allows for this to be overriden is all that is
    //needed to sendData to webView
    override fun sendData(message: String) {
        val tag = "android:switcher:" + R.id.container + ":" + 1
        val f : TrendsGraphFragment = supportFragmentManager.findFragmentByTag(tag) as TrendsGraphFragment
        f.displayReceivedData(message)
    }

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