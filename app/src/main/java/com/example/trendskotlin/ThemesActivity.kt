package com.example.trendskotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import kotlinx.android.synthetic.main.activity_themes.*

//Adapter tutorial taken from https://grokonez.com/android/kotlin-gridview-example-show-list-of-items-on-grid-android
class ThemesActivity: AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right)
    }

    var adapter: ThemeAdapter? = null
    var themesMap = mutableMapOf<String, Array<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("mode")

        //Load themes
        themesMap["Cryptocurrency"] = arrayOf("Coin", "Block", "Farm", "Regulation", "Payment", "Mining", "Hold", "Chain")
        themesMap["Technology"] = arrayOf("Tech", "Board", "Battery", "Facebook", "Fortnite", "Russia", "Collision", "Zuckerberg")
        themesMap["Summer"] = arrayOf("Beach", "Picnic", "Camp", "Sunshine", "Hot", "Jam", "Sweat", "BBQ")
        themesMap["Biology"] = arrayOf("Foot", "Hand", "Leg", "Arm", "Body")
        themesMap["Maths"] = arrayOf("Integrate", "Range", "Average", "Maximum")
        themesMap["Chemistry"] = arrayOf("Atom", "Radioactive", "Bond")
        themesMap["Physics"] = arrayOf("Momentum", "Gravity", "Power")
        themesMap["Languages"] = arrayOf("German", "French", "Spanish")
        themesMap["Economics"] = arrayOf("Currency", "Profit", "Loss")
        themesMap["Politics"] = arrayOf("Currency", "Profit", "Loss")
        themesMap["Google"] = arrayOf("Search", "Drive", "News", "Page", "Alphabet", "Plus", "YouTube", "Android")
        themesMap["Star Wars"] = arrayOf("Saber", "Force", "Blaster", "Jedi", "Speeder", "Space", "Emperor", "Projection")
        adapter = ThemeAdapter(this, ArrayList(themesMap.keys), themesMap, intent)

        grid_view_themes.adapter = adapter
    }

    class ThemeAdapter(context: Context, var themesList: ArrayList<String>, var themesMap: Map<String, Array<String>>, var lastIntent: Intent) : BaseAdapter() {
        var context: Context? = context

        override fun getCount(): Int {
            return themesList.size
        }

        override fun getItem(position: Int): Any {
            return themesList[0]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val theme = this.themesList[position]

            val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val themeView = inflator.inflate(R.layout.list_item_themes, null)

            val myButton = themeView.findViewById<Button>(R.id.ButtonTheme)

            myButton.setOnClickListener {
                pushGameActivity(theme)
            }

            myButton.text = theme

            return themeView
        }

        fun pushGameActivity(theme: String) {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("theme", theme)
            intent.putExtra("queries", themesMap[theme])
            Helpers.propagateIntent(lastIntent, intent, "mode")
            Helpers.propagateIntent(lastIntent, intent, "secondaryChoice")
            context!!.startActivity(intent)
        }

    }

}
