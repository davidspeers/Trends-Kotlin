package com.example.trendskotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View


import android.graphics.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.Toast
import android.R.attr.animation
import android.animation.*
import android.view.animation.AccelerateDecelerateInterpolator
import java.util.ArrayList
import android.R.attr.right
import android.R.attr.left
import android.support.constraint.solver.widgets.WidgetContainer.getBounds
import android.animation.ValueAnimator
import android.graphics.PixelFormat
import android.graphics.ColorFilter
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.drawable.Drawable




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
    var path2: Path? = null
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

            path2 = Path()
            path!!.moveTo(0f, rands[0])
            path!!.lineTo(viewWidth, rands[3])

            // Measure the path
            val measure = PathMeasure(path, false)
            length = measure.length

            val intervals = floatArrayOf(length, length)

            val animator = ObjectAnimator.ofFloat(this@PathView, "phase", 1f, 0f)
            animator.setDuration(5000)

            val animator2 = ObjectAnimator.ofFloat(this@PathView, "phase", 1f, 0f)
            animator2.setDuration(5000)

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    Log.d("Hello", "Animation Ended")
                    paint!!.color = Color.RED
                    //invalidate()
                    animator.start()
                }

                //Following Overrides Required Even When not used
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            /*this@PathView.animate().withEndAction {
                Log.d("Hello", "Animation Ended")
            }.start()*/

            animator.start()

            //otherPV.init()

            //Erase Line
//            paint!!.color = Color.RED
//            path!!.moveTo(0f, rands[0])
//            path!!.lineTo(viewWidth/3, rands[1])
//            path!!.lineTo(2*viewWidth/3, rands[2])
//            path!!.lineTo(viewWidth, rands[3])
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
        c.drawPath(path2, paint)
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

//class AnotherAnimatedView {
//    val animatorSet = AnimatorSet()
//
//    fun animate() {
//        animatorSet.playSequentially(createAnimatorList())
//        animatorSet.start()
//    }
//
//    private fun createAnimatorList(): List<Animator> {
//        val dataList = arrayOf(10, 20, 30)
//        val animatorList = ArrayList<Animator>()
//
//        dataList.forEach {
//            animatorList.add(createAnimator(it))
//        }
//
//        return animatorList
//    }
//
//    private fun createAnimator(drawData: DrawData): ValueAnimator {
//        val propertyX = PropertyValuesHolder.ofInt(PROPERTY_X, drawData.getStartX(), drawData.getStopX())
//        val propertyY = PropertyValuesHolder.ofInt(PROPERTY_Y, drawData.getStartY(), drawData.getStopY())
//        val propertyAlpha = PropertyValuesHolder.ofInt(PROPERTY_ALPHA, ALPHA_START, ALPHA_END)
//
//        val animator = ValueAnimator()
//        animator.setValues(propertyX, propertyY, propertyAlpha)
//        animator.duration = ANIMATION_DURATION.toLong()
//        animator.interpolator = AccelerateDecelerateInterpolator()
//
//        animator.addUpdateListener { valueAnimator -> this@AnimationManager.onAnimationUpdate(valueAnimator) }
//
//        return animator
//    }
//
//    private fun onAnimationUpdate(valueAnimator: ValueAnimator?) {
//        if (valueAnimator == null || listener == null) {
//            return
//        }
//
//        val x = valueAnimator.getAnimatedValue(PROPERTY_X) as Int
//        val y = valueAnimator.getAnimatedValue(PROPERTY_Y) as Int
//        val alpha = valueAnimator.getAnimatedValue(PROPERTY_ALPHA) as Int
//        val runningAnimationPosition = getRunningAnimationPosition()
//
//        val value = AnimationValue()
//        value.setX(x)
//        value.setY(y)
//        value.setAlpha(adjustAlpha(runningAnimationPosition, alpha))
//        value.setRunningAnimationPosition(runningAnimationPosition)
//
//        listener.onAnimationUpdated(value)
//        lastValue = value
//    }
//
//    private fun getRunningAnimationPosition(): Int {
//        val childAnimations = animatorSet.getChildAnimations()
//        for (i in childAnimations.indices) {
//            val animator = childAnimations.get(i)
//            if (animator.isRunning()) {
//                return i
//            }
//        }
//
//        return VALUE_NONE
//    }
//}

class PathView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
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

            val animator = ObjectAnimator.ofFloat(this@PathView2, "phase", 1f, 0f)
            animator.setDuration(5000)

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator) {
                    Log.d("Hello", "Animation Ended")
                    paint!!.color = Color.RED
                    //invalidate()
                }

                //Following Overrides Required Even When not used
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })

            /*this@PathView.animate().withEndAction {
                Log.d("Hello", "Animation Ended")
            }.start()*/

            animator.start()

            //Erase Line
//            paint!!.color = Color.RED
//            path!!.moveTo(0f, rands[0])
//            path!!.lineTo(viewWidth/3, rands[1])
//            path!!.lineTo(2*viewWidth/3, rands[2])
//            path!!.lineTo(viewWidth, rands[3])
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

internal class PathDrawable : Drawable(), AnimatorUpdateListener {
    private val mPath: Path
    private val mPaint: Paint
    private var mAnimator: ValueAnimator? = null

    init {
        mPath = Path()
        mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.setStrokeWidth(5f)
        mPaint.style = Paint.Style.STROKE
    }

    fun startAnimating() {
        val b = bounds
        mAnimator = ValueAnimator.ofInt(-b.bottom, b.bottom)
        mAnimator!!.duration = 2000
        mAnimator!!.addUpdateListener(this)
        mAnimator!!.start()
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(mPath, mPaint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(cf: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun onAnimationUpdate(animator: ValueAnimator) {
        mPath.reset()
        val b = bounds
        //mPath.moveTo(b.left.toFloat(), b.bottom.toFloat())
        mPath.moveTo(1000f, 1000f)

        /*mPath.quadTo((
            b.right.toFloat() - b.left) / 2,
            animator.currentPlayTime.toFloat(),
            b.right.toFloat(),
            b.bottom.toFloat()
        )*/
        mPath.lineTo(
            animator.animatedFraction * b.exactCenterX(),
            animator.animatedFraction * bounds.height()
        )
        invalidateSelf()
    }
}

