package com.example.trendskotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View

import android.graphics.*
import android.util.Log
import android.view.ViewTreeObserver
import android.animation.*

var viewHeight = 0
var viewWidth = 0f

class PathView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    var path: Path? = null
    var path2: Path? = null
    var paint: Paint? = null
    var length: Float = 0.toFloat()
    var length2: Float = 0.toFloat()

    var paintErase: Paint? = null

    fun init() {
        //this.dimensions is waiting for view dimensions to be available
        this.dimensions {

            viewHeight = it.first
            viewWidth = it.second.toFloat()

            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint!!.color = Color.BLUE
            paint!!.strokeWidth = 10f
            paint!!.style = Paint.Style.STROKE

            paintErase = Paint()
            paintErase!!.color = context.getColor(R.color.colorBackground) //Default Background Color - saved in colors.xml
            paintErase!!.strokeWidth = 11f //Slightly wider for antialiasing
            paintErase!!.style = Paint.Style.STROKE
            paintErase!!.alpha = 0

            Log.d("ViewDimensions", "Height: $viewHeight, Width: $viewWidth")

            var rands = listOf(
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat(),
                (0..viewHeight).random().toFloat())

            //Create Line
            path = Path()
            path!!.moveTo(-10f, rands[0])
            path!!.lineTo(viewWidth/3, rands[1])
            path!!.lineTo(2*viewWidth/3, rands[2])
            path!!.lineTo(viewWidth+10, rands[3])

            //Create Line
            path2 = Path()
            path2!!.moveTo(-10f, rands[0])
            path2!!.lineTo(viewWidth/3, rands[1])
            path2!!.lineTo(2*viewWidth/3, rands[2])
            path2!!.lineTo(viewWidth+10, rands[3])

            //Create Line
            //path2 = Path()
            //path2!!.moveTo(0f, rands[0])
            //path2!!.lineTo(viewWidth, rands[3])

            // Measure the path
            val measure = PathMeasure(path, false)
            length = measure.length
            //val measure2 = PathMeasure(path2, false)
            //length2 = measure2.length

            //val intervals = floatArrayOf(length, length)
            val animation = AnimatorSet() //change to false

            val animator = ObjectAnimator.ofFloat(this@PathView, "phase", 1f, 0f)
            animator.setDuration(5000)

            val animator2 = ObjectAnimator.ofFloat(this@PathView, "erase", 1f, 0f)
            animator2.setDuration(5000)

            //val view2 = findViewById<PathView2>(R.id.patheraser)

            //val animator2 = ObjectAnimator.ofFloat(view2, "phase", 1f, 0f)
            //animator2.setDuration(5000)

            animation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    Log.d("Hello", "Animation Ended")
                    //paint!!.color = Color.RED
                    //invalidate()
                    //view2.init()

                    rands = listOf(
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

                    val measure = PathMeasure(path, false)
                    length = measure.length

                    //animator2.start()
                    animation.start()
                }

                //Following Overrides Required Even When not used
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            animation.play(animator2).after(animator)
            animation.start()

            /*this@PathView.animate().withEndAction {
                Log.d("Hello", "Animation Ended")
            }.start()*/

            //animator.start()

            //otherPV.init()

            //Erase Line
//            paint!!.color = Color.RED
//            path!!.moveTo(0f, rands[0])
//            path!!.lineTo(viewWidth/3, rands[1])
//            path!!.lineTo(2*viewWidth/3, rands[2])
//            path!!.lineTo(viewWidth, rands[3])
        }
    }

    //is called by ObjectAnimator - ignore never used warning
    fun setPhase(phase: Float) {
        //Log.d("pathview", "setPhase called with:$phase")
        paint!!.pathEffect = createPathEffect(length, phase, 0.0f)
        invalidate()//will call onDraw
    }

    fun setErase(phase: Float) {
        paintErase!!.alpha = 255
        paintErase!!.pathEffect = createPathEffect(length, phase, 0.0f)
        invalidate()//will call onDraw
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
        c.drawPath(path, paintErase)
        //c.drawPath(path, paintErase)
        //c.drawLines(floatArrayOf(0f, 20f, 1000f, 1000f), paint)
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
}