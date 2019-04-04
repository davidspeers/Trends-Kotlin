package com.example.trendskotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EmptyActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Results"
    }
}
