package com.example.trendskotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View


import android.animation.ObjectAnimator
import android.graphics.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationSet

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

var viewHeight = 0
var viewWidth = 0f

class PathView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var path: Path? = null
    var paint: Paint? = null
    var length: Float = 0.toFloat()

    fun init() {
        //this.dimensions is waiting for view dimensions to be available
        this.dimensions {

            viewHeight = it.first
            viewWidth = it.second.toFloat()

            paint = Paint()
            paint!!.color = Color.BLUE
            paint!!.strokeWidth = 10f
            paint!!.style = Paint.Style.STROKE

            Log.d("ViewDimensions", "Height: $viewHeight, Width: $viewWidth")

            val rands = listOf(
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat())

            //Create Line
            path = Path()
            path!!.moveTo(0f, rands[0])
            path!!.lineTo(viewWidth/3, rands[1])
            path!!.lineTo(2*viewWidth/3, rands[2])
            path!!.lineTo(viewWidth, rands[3])

            // Measure the path
            val measure = PathMeasure(path, false)
            length = measure.length

            val intervals = floatArrayOf(length, length)

            var animations = AnimationSet(true)

            val animator = ObjectAnimator.ofFloat(this@PathView, "phase", 1f, 0f)
            animator.setDuration(5000)
            animator.start()

            //Erase Line
            paint!!.color = Color.RED
            path!!.moveTo(0f, rands[0])
            path!!.lineTo(viewWidth/3, rands[1])
            path!!.lineTo(2*viewWidth/3, rands[2])
            path!!.lineTo(viewWidth, rands[3])
        }
    }

    //is called by animtor object
    fun setPhase(phase: Float) {
        Log.d("pathview", "setPhase called with:$phase")
        paint!!.pathEffect = createPathEffect(length, phase, 0.0f)
        invalidate()//will calll onDraw
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(
            floatArrayOf(pathLength, pathLength),
            Math.max(phase * pathLength, offset)
        )
    }

    public override fun onDraw(c: Canvas) {
        super.onDraw(c)
        //c.drawPath(path, paint)
        c.drawPath(path, paint)
        c.drawLines(floatArrayOf(0f, 20f, 1000f, 1000f), paint)
    }

    fun <T : View> T.height(function: (Int) -> Unit) {
        if (height == 0)
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    function(height)
                }
            })
        else function(height)
    }

    fun <T : View> T.dimensions(function: (Pair<Int, Int>) -> Unit) {
        if (height == 0 && width == 0)
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    function(Pair(height, width))
                }
            })
        else function(Pair(height, width))
    }

    /*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        viewHeight = parentHeight
        viewWidth = parentWidth
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)



        Log.d("onSizeChanged2", "New Dimensions: $viewHeight, $viewWidth")
    }*/

}
