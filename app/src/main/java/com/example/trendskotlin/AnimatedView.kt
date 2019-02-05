package com.example.trendskotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class AnimatedView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val startX = 0
    private val startY = 0

    private var endX = 0
    private var endY = 0

    private val paint = object : Paint(Paint.ANTI_ALIAS_FLAG) {
        init {
            isDither = true
            color = Color.RED
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), paint)

        if (endX != 300 && endY != 300) { // set end points
            endY++
            endX++

            invalidate()

            //postInvalidateDelayed(0) // set time here
        }
    }
}
