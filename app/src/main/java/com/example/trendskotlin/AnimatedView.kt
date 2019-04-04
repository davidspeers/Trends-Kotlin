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
    var paths = listOf(Path(), Path())
    var rands = listOf<Float>() //need Int for random() and Float for moveTo and lineTo
    var lengths = arrayListOf(0f, 0f)

    var colors = listOf(
        Color.BLUE,
        Color.RED,
        Color.MAGENTA,
        Color.GREEN
    )

    var paints = listOf(
        Paint(),
        Paint()
    )

    var paintErase = Paint()

    fun init() {
        //this.dimensions is waiting for view dimensions to be available
        this.dimensions {

            viewHeight = it.first
            viewWidth = it.second.toFloat()

            //Init Paints
            colors = colors.shuffled()
            paints.forEachIndexed { index, paint ->
                paint.color = colors[index]
                paint.isAntiAlias = true
                paint.strokeWidth = 10f
                paint.style = Paint.Style.STROKE
            }

            //Init PaintErase
            paintErase.color = context.getColor(R.color.colorBackground) //Default Background Color - saved in colors.xml
            paintErase.strokeWidth = 11f //Slightly wider for antialiasing
            paintErase.style = Paint.Style.STROKE
            paintErase.alpha = 0

            //Log.d("ViewDimensions", "Height: $viewHeight, Width: $viewWidth")

            val animations = AnimatorSet()

            //Create Line
            paths.forEachIndexed { index, path ->
                rands = listOf(
                    (0..viewHeight).random().toFloat(),
                    (0..viewHeight).random().toFloat(),
                    (0..viewHeight).random().toFloat(),
                    (0..viewHeight).random().toFloat())

                path.moveTo(-10f, rands[0])
                path.lineTo(viewWidth/3, rands[1])
                path.lineTo(2*viewWidth/3, rands[2])
                path.lineTo(viewWidth+10, rands[3])

                // Measure the path
                val measure = PathMeasure(path, false)
                lengths[index] = measure.length
            }

            val animator = ObjectAnimator.ofFloat(this@PathView, "phase", 1f, 0f)
            animator.duration = 4000

            val animator2 = ObjectAnimator.ofFloat(this@PathView, "phase2", 1f, 0f)
            animator2.duration = 4000

            val animatorErase = ObjectAnimator.ofFloat(this@PathView, "erase", 1f, 0f)
            animatorErase.duration = 4000

            animations.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {

                    colors = colors.shuffled()
                    paints.forEachIndexed { index, paint ->
                        paint.color = colors[index]
                    }

                    //Create Lines
                    paths.forEachIndexed { index, path ->
                        path.reset() //remove previous lineTo and moveTo

                        rands = listOf(
                            (0..viewHeight).random().toFloat(),
                            (0..viewHeight).random().toFloat(),
                            (0..viewHeight).random().toFloat(),
                            (0..viewHeight).random().toFloat())

                        path.moveTo(-10f, rands[0])
                        path.lineTo(viewWidth/3, rands[1])
                        path.lineTo(2*viewWidth/3, rands[2])
                        path.lineTo(viewWidth+10, rands[3])

                        // Measure the path
                        val measure = PathMeasure(path, false)
                        lengths[index] = measure.length
                    }

                    animations.start()
                }

                //Following Overrides Required Even When not used
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            animations.play(animator2)
            animations.play(animatorErase).after(animator)
            animations.start()

        }
    }

    //is called by ObjectAnimator - ignore never used warning
    fun setPhase(phase: Float) {
        paints[0].pathEffect = createPathEffect(lengths[0], phase, 0.0f)
        invalidate()//will call onDraw
    }

    fun setPhase2(phase: Float) {
        paints[1].pathEffect = createPathEffect(lengths[1], phase, 0.0f)
        invalidate()//will call onDraw
    }

    fun setErase(phase: Float) {
        paintErase.alpha = 255
        paintErase.pathEffect = createPathEffect(lengths.max()!!, phase, 0.0f)
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
        //Log.d("Animation", "Animation onDraw called") //method is called multiple times a second
        paths.forEachIndexed {index, path ->
            c.drawPath(path, paints[index])
            c.drawPath(path, paintErase)
        }
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