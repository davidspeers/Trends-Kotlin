package com.example.trendskotlin

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class GameActivity : AppCompatActivity(), GamePresenter.View {

    //Initialise Presenter
    private val presenter: GamePresenter = GamePresenter()

    //Instantiate EditText
    var editText : EditText? = null

    //Instantiate FAB
    var fab : FloatingActionButton? = null

    //Instantiate Changing Text Views
    var teamText : TextView? = null
    var queryText : TextView? = null

    //Instantiate intents
    var mode : String? = null
    var secondaryChoice : String? = null
    var queries : Array<String>? = null

    //Track Variables
    var teamNum = 1
    var queryNum = 0
    var terms = arrayOf<String>()

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("theme")

        presenter.attachView(this)

        //Initialise UI Variables
        editText = findViewById(R.id.editText)
        editText?.showKeyboard()
        fab = findViewById(R.id.fab)
        teamText = findViewById(R.id.TeamName)
        queryText = findViewById(R.id.Query)

        //Initialise Intents
        mode = intent.getStringExtra("mode")
        secondaryChoice = intent.getStringExtra("secondaryChoice")
        queries = intent.getStringArrayExtra("queries")

        queryText?.text = queries!![queryNum]

        //Set onClick Listeners
        fab?.setOnClickListener {
            presenter.enterTerm(editText!!.text.toString())
        }
    }

    override fun onRestart() {
        super.onRestart()
        queryText?.text = queries!![queryNum]
        teamText!!.text = "Team ${teamNum}"
        editText?.showKeyboard()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    override fun showTermRequiredMessage() {
        Toast.makeText(this, "Term Can't be Empty", Toast.LENGTH_SHORT).show()
    }

    fun pushResultsActivity() {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("query", queryText?.text)
        intent.putExtra("terms", terms)
        this.startActivity(intent)
    }

    override fun pushTerm(term: String) {
        if (mode == "Party Mode") {
            terms += editText!!.text.toString()
            if (teamNum < secondaryChoice!!.toInt()) {
                //Update TextViews
                teamText!!.text = teamText!!.text.replace(teamNum.toString().toRegex(), (teamNum+1).toString())
                teamNum++
            } else {
                queryNum++
                teamNum = 1
                pushResultsActivity()
            }
            //Update EditView
            editText!!.text.clear()
        }
    }



    fun View.showKeyboard() {
        //Note: for this to work you need to add android:windowSoftInputMode="stateVisible"
        // to the activity tag in the android manifest. But this didn't allow for FAB alignment so
        // android:windowSoftInputMode="adjustResize" achieved both
        this.requestFocus() //can be done in the XML file instead
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

}
