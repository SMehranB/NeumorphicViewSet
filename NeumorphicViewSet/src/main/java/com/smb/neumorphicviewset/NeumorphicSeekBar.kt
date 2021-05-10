package com.smb.neumorphicviewset

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

class NeumorphicSeekBar : View, NeuUtil {
    constructor(context: Context): super(context){
        initAttributes(context, null, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        initAttributes(context, attributeSet, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        initAttributes(context, attributeSet, defStyleAttr)
    }

    enum class Jut { SMALL, NORMAL, LARGE }

    private var handleRadius: Float = dpToPixel(context, 12)

    /* Paint objects */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private var mBackgroundColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
    private val backgroundRectF = RectF()
    private val thickness = dpToPixel(context, 5)
    private var cornerRadius: Float = dpToPixel(context, 100)
    private var horizontalPadding: Float = dpToPixel(context,16)
    private var verticalPadding: Float = dpToPixel(context,16)
    var disabledTextColor = Color.GRAY

    private var progressColor: Int = Color.CYAN
    private var progress: Float = 0f
    private var handleX: Float = 0f
    private var handleY: Float = 0f
    private var isDragging = false

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context,16)
    private var lightDensity: Float = 0.6f
    private var shadowDensity: Float = 0.4f
    private var jutSize: Int = 1
    private var jut: NeumorphicImageButton.Jut = NeumorphicImageButton.Jut.NORMAL


    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeumorphicImageButton, defStyleAttr, 0)
        attrs.apply {
//            cornerRadius = getDimension(R.styleable.NeumorphicImageButton_nib_cornerRadius, cornerRadius)
//            mBackgroundColor = getInteger(R.styleable.NeumorphicImageButton_nib_backgroundColor, mBackgroundColor)
//
//            drawableDimension = getDimension(R.styleable.NeumorphicImageButton_nib_drawableDimension, drawableDimension)
//            drawable = getResourceId(R.styleable.NeumorphicImageButton_nib_drawable, drawable)
//            drawableTint = getInteger(R.styleable.NeumorphicImageButton_nib_drawableTint, drawableTint)
//
//            lightDensity = getFloat(R.styleable.NeumorphicImageButton_nib_lightDensity, lightDensity).coerceAtMost(1f)
//            shadowDensity = getFloat(R.styleable.NeumorphicImageButton_nib_shadowDensity, shadowDensity).coerceAtMost(1f)
//            jutSize = getInt(R.styleable.NeumorphicImageButton_nib_JutSize, jutSize)
//
//            horizontalPadding = getDimension(R.styleable.NeumorphicImageButton_nib_HorizontalPadding, horizontalPadding)
//            verticalPadding = getDimension(R.styleable.NeumorphicImageButton_nib_VerticalPadding, verticalPadding)
//            disabledTextColor = getInteger(R.styleable.NeumorphicImageButton_nib_disabledColor, disabledTextColor)
//
//            isEnabled = getBoolean(R.styleable.NeumorphicImageButton_nib_enabled, true)

            recycle()
        }

        when (jutSize) {
            0 -> jut = NeumorphicImageButton.Jut.SMALL
            1 -> jut = NeumorphicImageButton.Jut.NORMAL
            2 -> jut = NeumorphicImageButton.Jut.LARGE
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredDimensions = getDesiredDimensions()

        val desiredWidth = desiredDimensions.width
        val desiredHeight = desiredDimensions.height

        setMeasuredDimension(getFinalDimension(desiredWidth, widthMeasureSpec),
            getFinalDimension(desiredHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {


        setJutSize(jut)


        setLayerTypeBasedOnSDK(this, lightPaint)

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {

            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, progressPaint)

            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)

            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)

            drawCircle(handleX, handleY, handleRadius, handlePaint)
            drawCircle(handleX, handleY, handleRadius - thickness, progressPaint)
        }
    }

    private fun seekBarClicked(event: MotionEvent): Boolean {
        return event.x in backgroundRectF.left..backgroundRectF.right &&
                event.y in backgroundRectF.top..backgroundRectF.bottom
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null && isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return if (seekBarClicked(event)) {
                        parent.requestDisallowInterceptTouchEvent(true)

                        handleX = event.x
                        isDragging = true

                        invalidate()
                        performClick()
                        true
                    } else {
                        isDragging = false
                        false
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) {
                        handleX = event.x
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    isDragging = false
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** The size of the view is calculated based on
         * the height and the width of the drawable
         */

        val width = resources.displayMetrics.widthPixels
        val height = dpToPixel(context, 20)
            .plus(shadowMargin.times(2))

        return NeuUtil.MinimumDimensions(width, height.toInt())
    }

    private fun setJutSize(jut: NeumorphicImageButton.Jut) {

        var radius = 0f
        var lightOffset = 0f
        var shadowOffset = 0f

        when (jut) {
            NeumorphicImageButton.Jut.SMALL -> {
                radius = 25f
                lightOffset = 7f
                shadowOffset = 8f
            }

            NeumorphicImageButton.Jut.NORMAL -> {
                radius = 25f
                lightOffset = 9f
                shadowOffset = 8f
            }

            NeumorphicImageButton.Jut.LARGE -> {
                radius = 25f
                lightOffset = 10f
                shadowOffset = 11f
            }
        }

        backgroundRectF.set(shadowMargin, shadowMargin, width.minus(shadowMargin), height.minus(shadowMargin))
        backgroundPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = thickness
            color = mBackgroundColor
            setShadowLayer(radius.minus(2), shadowOffset, shadowOffset, ColorUtils.blendARGB(mBackgroundColor, Color.BLACK, shadowDensity))
        }

        progressPaint.apply {
            style = Paint.Style.FILL
            color = progressColor
        }

        lightPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = thickness
            color = mBackgroundColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(mBackgroundColor, Color.WHITE, lightDensity))
        }

        handlePaint.apply {
            handleX = backgroundRectF.left.plus(thickness)
            handleY = height.div(2f)
            style = Paint.Style.STROKE
            strokeWidth = thickness.times(2f)
            color = mBackgroundColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(mBackgroundColor, Color.BLACK, shadowDensity))
        }
    }

}