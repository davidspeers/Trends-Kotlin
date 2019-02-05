package com.example.trendskotlin

import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Toast

/**
 * Put Helper functions in Helpers object (java static equivalent)
 */
object Helpers {
    fun propagateIntent(lastIntent: Intent, intent: Intent, name: String) {
        intent.putExtra(name, lastIntent.getStringExtra(name))
    }
}

