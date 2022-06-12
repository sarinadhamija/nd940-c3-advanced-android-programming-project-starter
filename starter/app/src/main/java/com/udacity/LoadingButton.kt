package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var sweepAngle = 0f
    private var currentWidth = 0

    private var defaultText = ""
    private var loadingText = ""

    private var textColor: Int = 0
    private var arcColor: Int = 0
    private var buttonProgressColor: Int = 0
    private var buttonDefaultColor: Int = 0

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            defStyleAttr,
            defStyleAttr
        )
        defaultText = typedArray.getString(R.styleable.LoadingButton_loader_default_text).toString()
        loadingText =
            typedArray.getString(R.styleable.LoadingButton_loader_progress_text).toString()
        arcColor = typedArray.getColor(R.styleable.LoadingButton_arc_color, Color.WHITE)
        buttonProgressColor = typedArray.getColor(
            R.styleable.LoadingButton_button_background_progress_color,
            Color.CYAN
        )
        textColor = typedArray.getColor(
            R.styleable.LoadingButton_button_text_color,
            Color.WHITE
        )
        buttonDefaultColor = typedArray.getColor(
            R.styleable.LoadingButton_button_background_default_color,
            Color.WHITE
        )
        typedArray.recycle()
    }

    private val paint = Paint().apply {
        color = textColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        textSize = 50F
        textAlign = Paint.Align.CENTER
    }

    private val circlePaint = Paint().apply {
        color = Color.TRANSPARENT
        style = Paint.Style.FILL
    }

    private val arcPaint = Paint().apply {
        color = arcColor
        style = Paint.Style.FILL
    }

    private val primaryButtonPaint = Paint().apply {
        color = buttonProgressColor
        style = Paint.Style.FILL
    }

    private var buttonBackgroundAnimator: ValueAnimator? = null

    private val circularAnimation = ValueAnimator.ofInt(0, 360).apply {
        interpolator = DecelerateInterpolator()
        duration = 1000
        addUpdateListener {
            sweepAngle = (it.animatedValue as Int).toFloat()
            invalidate()
        }
    }

    private fun animateLoaderButton() {
        circularAnimation.start()
        buttonBackgroundAnimator?.start()
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when (new) {
            ButtonState.Completed -> {
                invalidate()
            }

            ButtonState.Loading -> {
                animateLoaderButton()
            }
        }
    }

    fun updateButtonState(buttonState: ButtonState) {
        this.buttonState = buttonState
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {

            when (buttonState) {
                ButtonState.Loading -> {
                    drawColor(buttonDefaultColor)
                    drawRect(0f, 0f, currentWidth.toFloat(), height.toFloat(), primaryButtonPaint)

                    val xPos = (width / 2).toFloat()
                    val yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
                    val radius = 30f

                    val xyz = loadingText
                    drawText(xyz, xPos, yPos, paint)

                    drawCircle(xPos + 280, yPos - 15f, 30f, circlePaint)

                    val rectf = RectF()
                    rectf.left = (xPos + (280)) - radius
                    rectf.right = (xPos + (280)) + radius
                    rectf.top = (yPos - 15f) - radius
                    rectf.bottom = (yPos - 15f) + radius

                    drawArc(rectf, 0f, sweepAngle, true, arcPaint)
                }

                ButtonState.Completed -> {
                    drawColor(buttonDefaultColor)

                    val xPos = (width / 2).toFloat()
                    val yPos = (height / 2 - (paint.descent() + paint.ascent()) / 2)
                    drawText(defaultText, xPos, yPos, paint)
                }
            }
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
        setBackgroundAnimator()
    }

    private fun setBackgroundAnimator() {
        buttonBackgroundAnimator = ValueAnimator.ofInt(0, widthSize).apply {
            interpolator = DecelerateInterpolator()
            duration = 1000
            addUpdateListener {
                currentWidth = (it.animatedValue as Int)
                invalidate()
            }
        }

    }

}