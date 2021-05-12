package com.smb.neumorphicviewset

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.smb.neumorphicviewset.interfaces.NeuUtil
import com.smb.neumorphicviewset.interfaces.OnNeuSeekBarChangeListener
import kotlin.math.abs

class NeuSeekBar : View, NeuUtil {
    constructor(context: Context): super(context){
        initAttributes(context, null, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        initAttributes(context, attributeSet, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        initAttributes(context, attributeSet, defStyleAttr)
    }

    /* Paint objects */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    var backColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }
    private val backgroundRectF = RectF()
    private val progressRectF = RectF()
    private val thickness = dpToPixel(context, 5)
    private var cornerRadius: Float = dpToPixel(context, 100)
    var disabledColor = Color.GRAY

    /* Progress and ProgressBar Parameters*/
    var progressColor: Int = Color.CYAN
        set(value) {
            field = value
            progressPaint.color = value
            invalidate()
        }
    private var progressBarHeight = dpToPixel(context, 8)
    private var handleRadius: Float = dpToPixel(context, 8)
    private val handleGlowRadius = 25f
    private var progressBarStart = 0f
    private var progressBarEnd = 0f
    private var progressBarRange = 0f
    private var progress: Int = 0
    private var handleX: Float = 0f
    private var handleY: Float = 0f
    private var isDragging = false
    var min: Int = 0
    var max: Int = 100

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context,16)
    private var lightDensity: Float = 0.5f
    private var shadowDensity: Float = 0.5f
    private var jutSize: Int = 1
    private var jut: Jut = Jut.NORMAL

    private var onNeuSeekBarChangeListener: OnNeuSeekBarChangeListener? = null


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredDimensions = getDesiredDimensions()

        val desiredWidth = desiredDimensions.width
        val desiredHeight = desiredDimensions.height

        setMeasuredDimension(getFinalDimension(desiredWidth, widthMeasureSpec),
            getFinalDimension(desiredHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        /**
         * The order of the following functions is important
         */
        adjustBackgroundParams()
        adjustJutParams(jut)
        adjustProgressBarParams()

        setLayerTypeBasedOnSDK(this, lightPaint)

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {
            // DRAWING BACKGROUND
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

            // DRAWING PROGRESS
            drawRoundRect(progressRectF, cornerRadius, cornerRadius, progressPaint)

            // DRAWING HANDLE
            drawCircle(handleX, handleY, handleRadius, lightPaint)
            drawCircle(handleX, handleY, handleRadius, handlePaint)
            drawCircle(handleX, handleY, handleRadius - thickness, progressPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null && isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return if (seekBarClicked(event)) {

                        parent.requestDisallowInterceptTouchEvent(true)
                        progress = xToProgress(event.x)
                        isDragging = true
                        onNeuSeekBarChangeListener?.onStartTrackingTouch(this)

                        if (clickInProgressArea(event)) {
                            handleX = event.x
                            adjustProgressRectF()
                            invalidate()
                        }

                        performClick()
                        true

                    } else {
                        isDragging = false
                        false
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isDragging && seekBarClicked(event)) {

                        progress = xToProgress(event.x)
                        onNeuSeekBarChangeListener?.onProgressChanged(this, progress, true)

                        if (clickInProgressArea(event)) {
                            handleX = event.x
                            adjustProgressRectF()
                            invalidate()
                        }
                    }
                }

                MotionEvent.ACTION_UP -> {
                    isDragging = false
                    onNeuSeekBarChangeListener?.onStopTrackingTouch(this)
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }

        return super.onTouchEvent(event)
    }

    fun setOnSeekBarProgressChanged(onNeuSeekBarChangeListener: OnNeuSeekBarChangeListener) {
        this.onNeuSeekBarChangeListener = onNeuSeekBarChangeListener
    }

    fun enable() {
        progressPaint.apply {
            color = progressColor
            setShadowLayer(handleGlowRadius, 0f ,0f , progressColor)
        }
        isEnabled = true
        invalidate()
    }

    fun disable() {
        progressPaint.apply {
            color = disabledColor
            clearShadowLayer()
        }
        isEnabled = false
        invalidate()
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        handleX = progressToX(progress)
        adjustProgressRectF()
        onNeuSeekBarChangeListener?.onProgressChanged(this, progress, false)
        invalidate()
    }

    fun getProgress(): Int {
        return progress
    }

    fun setHandleRadius(radiusDp: Int) {
        handleRadius = dpToPixel(context, radiusDp)
        invalidate()
    }

    fun setJutParams(jut: Jut) {
        this.jut = jut
        adjustJutParams(jut)
        invalidate()
    }

    fun setJutParams(lightDensity: Float, shadowDensity: Float) {
        this.lightDensity = lightDensity
        this.shadowDensity = shadowDensity
        adjustJutParams(jut)
        invalidate()
    }

    fun setJutParams(lightDensity: Float, shadowDensity: Float, jut: Jut) {
        this.lightDensity = lightDensity
        this.shadowDensity = shadowDensity
        this.jut = jut
        adjustJutParams(jut)
        invalidate()
    }

    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuSeekBar, defStyleAttr, 0)
        attrs.apply {

            handleRadius = getDimension(R.styleable.NeuSeekBar_nsb_handleRadius, handleRadius)
            backColor = getInteger(R.styleable.NeuSeekBar_nsb_backgroundColor, backColor)

            lightDensity = getFloat(R.styleable.NeuSeekBar_nsb_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuSeekBar_nsb_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuSeekBar_nsb_JutSize, jutSize)

            progress = getInt(R.styleable.NeuSeekBar_nsb_Progress, progress)
            progressBarHeight = getDimension(R.styleable.NeuSeekBar_nsb_ProgressBarHeight, progressBarHeight)
            min = getInt(R.styleable.NeuSeekBar_nsb_Min, min)
            max = getInt(R.styleable.NeuSeekBar_nsb_Max, max)
            progressColor = getInt(R.styleable.NeuSeekBar_nsb_ProgressColor, progressColor)

            disabledColor = getInteger(R.styleable.NeuSeekBar_nsb_disabledColor, disabledColor)

            isEnabled = getBoolean(R.styleable.NeuSeekBar_nsb_enabled, true)

            recycle()
        }

        when (jutSize) {
            0 -> jut = Jut.SMALL
            1 -> jut = Jut.NORMAL
            2 -> jut = Jut.LARGE
        }
    }

    private fun xToProgress(x: Float): Int {

        val offsetX = x.minus(progressBarStart)

        return when {
            offsetX <= 0f -> {
                min
            }

            offsetX in progressBarRange..width.toFloat() -> {
                max
            }

            else -> {
                val progressRange = max.minus(min)
                (min + offsetX.div(progressBarRange).times(progressRange)).toInt()
            }
        }
    }

    private fun progressToX(progress: Int): Float {
        if (progress !in min..max) throw IllegalArgumentException("Input must be in range [min, max]")
        if(progress == min) return progressBarStart
        if (progress == max) return progressBarEnd

        val rawProgress = abs(min - progress).toFloat()
        val progressRange = max.minus(min)
        val ratio: Float = rawProgress.div(progressRange)

        return progressBarRange.times(ratio) + progressBarStart
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** The size of the view is calculated based on
         * the size of the handle and progressBar height
         * (whichever is larger in size)
         */

        val width = dpToPixel(context, 100)
        val height = (progressBarHeight.coerceAtLeast(handleRadius.times(2))) + (shadowMargin.times(2)) + thickness

        return NeuUtil.MinimumDimensions(width.toInt(), height.toInt())
    }

    private fun adjustProgressBarParams() {
        progressPaint.apply {
            style = Paint.Style.FILL
            color = progressColor
            setShadowLayer(handleGlowRadius, 0f ,0f , progressColor)
        }

        progressBarStart = backgroundRectF.left.plus(thickness.div(2))
        progressBarEnd = backgroundRectF.right.minus(thickness.div(2))
        progressBarRange = progressBarEnd.minus(progressBarStart)
    }

    private fun adjustProgressRectF() {
        progressRectF.set(progressBarStart, backgroundRectF.top.plus(thickness.div(2)),
            handleX, backgroundRectF.bottom.minus(thickness.div(2)))
    }

    private fun adjustBackgroundParams() {

        val barLeft = handleRadius.plus(shadowMargin)
        val barRight = width.minus(barLeft)
        val barTop = height.div(2).minus(progressBarHeight.div(2))
        val barBottom = height.div(2).plus(progressBarHeight.div(2))

        backgroundRectF.set(barLeft, barTop, barRight, barBottom)

        cornerRadius = backgroundRectF.height().div(2)
        backgroundPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = thickness
            color = backColor
        }
    }

    private fun adjustJutParams(jut: Jut) {

        var radius = 0f
        var lightOffset = 0f
        var shadowOffset = 0f

        when (jut) {
            Jut.SMALL -> {
                radius = 25f
                lightOffset = 7f
                shadowOffset = 8f
            }

            Jut.NORMAL -> {
                radius = 20f
                lightOffset = 8f
                shadowOffset = 10f
            }

            Jut.LARGE -> {
                radius = 25f
                lightOffset = 10f
                shadowOffset = 11f
            }
        }

        backgroundPaint.apply {
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(backColor, Color.BLACK, shadowDensity))
        }

        lightPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = thickness
            color = backColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(backColor, Color.WHITE, lightDensity))
        }

        handleX = backgroundRectF.left
        handleY = height.div(2f)
        handlePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = thickness.times(2f)
            color = backColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(backColor, Color.BLACK, shadowDensity))
        }
    }

    private fun seekBarClicked(event: MotionEvent): Boolean {
        return event.x in 0f..width.toFloat()
    }

    private fun clickInProgressArea(event: MotionEvent): Boolean {
        return event.x in progressBarStart..progressBarEnd
    }
}