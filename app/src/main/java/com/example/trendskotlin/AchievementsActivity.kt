package com.example.trendskotlin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class AchievementsActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Achievements"

        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = CustomAdapter(this, achievements(), achievementsDescription())
    }

    private fun achievements() : List<String> {
        val data = arrayListOf(
            "Unlocked Achievements 2/15",
            "Trends Newbie",
            "Trends with Friends",
            "Locked Achievements 13/15",
            "Trends Novice",
            "Trends Pro",
            "Trends Setter",
            "Pro Trends Setter",
            "Trends Getter",
            "Complete all Themes",
            "Beat the Machine"
        )
        return data
    }

    private fun achievementsDescription() : List<String> {
        val data = arrayListOf(
            "",
            "Play 1 Game",
            "Play a game of party mode",
            "",
            "Play 10 games",
            "Play 100 games",
            "Create a custom theme",
            "Create 5 custom themes",
            "Play a custom theme",
            "Play every theme",
            "Win a game of CPU mode"
        )
        return data
    }

    class CustomAdapter(context: Context, private var titles: List<String>, private var bodies: List<String>) :
            ArrayAdapter<String>(context, R.layout.list_item_achievements, titles) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var item = convertView
            if (item == null) {
                item = when (position) {
                    0,3 -> {
                        LayoutInflater.from(context).inflate(R.layout.list_header_achievements, parent, false)
                    }
                    else -> {
                        LayoutInflater.from(context).inflate(R.layout.list_item_achievements, parent, false)
                    }
                }
            }
            val titleView = item?.findViewById<TextView>(R.id.achieve_title)
            titleView?.text = titles[position]
            val bodyView = item?.findViewById<TextView>(R.id.achieve_body)
            bodyView?.text = bodies[position]
            return item!!
        }
    }

}
