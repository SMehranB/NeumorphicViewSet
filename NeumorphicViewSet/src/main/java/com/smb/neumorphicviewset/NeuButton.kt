package com.smb.neumorphicviewset

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import com.smb.neumorphicviewset.interfaces.NeuUtil

class NeuButton : View, NeuUtil {

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
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private var mBackgroundColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
    private var cornerRadius: Float = dpToPixel(context, 8)
    private val backgroundRectF = RectF()

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context, 16)
    private var lightDensity: Float = 0.5f
    private var shadowDensity: Float = 0.5f
    private var jutSize: Int = 1
    private var jut: Jut = Jut.NORMAL

    /* Drawable Parameters */
    private lateinit var drawableStartBitmap: Bitmap
    private lateinit var drawableEndBitmap: Bitmap
    private var drawablePadding: Float = dpToPixel(context, 8)
    private var drawableStartX: Float = 0f
    private var drawableEndX: Float = 0f
    private var drawablesY: Float = 0f
    private var drawableDimension:Float = dpToPixel(context, 25)
    private var drawableStart: Int = 0
    private var drawableEnd: Int = 0
    private var drawableTint: Int = 0

    /* Text parameters */
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var textXOffSet: Float = 0f
    private var horizontalPadding: Float = dpToPixel(context, 16)
    private var verticalPadding: Float = dpToPixel(context, 16)
    private var textHeight: Float = 0f
    var textSizeDp: Float = dpToPixel(context, 16)
        set(value) {
            field = value
            requestLayout()
        }
    @ColorInt
    var textColor: Int = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }
    @StyleRes
    var textStyle: Int = Typeface.NORMAL
        set(value) {
            field = value
            requestLayout()
        }
    @FontRes
    var textFont: Int = 0
        set(value) {
            field = value
            requestLayout()
        }
    var disabledTextColor = Color.GRAY
    var text: String = "Neumorphic Button"
        set(value) {
            field = value
            requestLayout()
        }


    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuButton, defStyleAttr, 0)
        attrs.apply {
            cornerRadius = getDimension(R.styleable.NeuButton_nb_cornerRadius, cornerRadius)
            mBackgroundColor = getInteger(R.styleable.NeuButton_nb_backgroundColor, mBackgroundColor)

            drawableDimension = getDimension(R.styleable.NeuButton_nb_drawableDimension, drawableDimension)
            drawableStart = getResourceId(R.styleable.NeuButton_nb_drawableStart, 0)
            drawableEnd = getResourceId(R.styleable.NeuButton_nb_drawableEnd, 0)
            drawablePadding = getDimension(R.styleable.NeuButton_nb_drawablePadding, drawablePadding)
            drawableTint = getInteger(R.styleable.NeuButton_nb_drawableTint, drawableTint)

            lightDensity = getFloat(R.styleable.NeuButton_nb_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuButton_nb_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuButton_nb_JutSize, jutSize)

            horizontalPadding = getDimension(R.styleable.NeuButton_nb_HorizontalPadding, horizontalPadding)
            verticalPadding = getDimension(R.styleable.NeuButton_nb_VerticalPadding, verticalPadding)
            textStyle = getInt(R.styleable.NeuButton_nb_textStyle, textStyle)
            textSizeDp = getDimension(R.styleable.NeuButton_nb_textSize, textSizeDp)
            textColor = getInteger(R.styleable.NeuButton_nb_textColor, textColor)
            textFont = getResourceId(R.styleable.NeuButton_nb_fontFamily, 0)
            disabledTextColor = getInteger(R.styleable.NeuButton_nb_disabledTextColor, disabledTextColor)
            text = getString(R.styleable.NeuButton_nb_text) ?: text

            isEnabled = getBoolean(R.styleable.NeuButton_nb_enabled, true)

            recycle()
        }

        when (jutSize) {
            0 -> jut = Jut.SMALL
            1 -> jut = Jut.NORMAL
            2 -> jut = Jut.LARGE
        }

        setLayerTypeBasedOnSDK(this, lightPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val desiredDimensions = getDesiredDimensions()

        val desiredWidth = desiredDimensions.width
        val desiredHeight = desiredDimensions.height

        setMeasuredDimension(getFinalDimension(desiredWidth, widthMeasureSpec),
            getFinalDimension(desiredHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        adjustDrawables(drawableTint)
        adjustJutParams(jut)
        adjustText()

        backgroundRectF.set(shadowMargin, shadowMargin, width.minus(shadowMargin), height.minus(shadowMargin))
        backgroundPaint.color = mBackgroundColor

        if (!isEnabled) {
            disable()
        }

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, shadowPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)
            drawText(text, mTextX, mTextY, textPaint)
            if (drawableStart != 0) {
                drawBitmap(drawableStartBitmap, drawableStartX, drawablesY, drawablePaint)
            }
            if (drawableEnd != 0) {
                drawBitmap(drawableEndBitmap, drawableEndX, drawablesY, drawablePaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event?.action == MotionEvent.ACTION_DOWN && isEnabled){

            if(event.x in backgroundRectF.left..backgroundRectF.right &&
                event.y in backgroundRectF.top..backgroundRectF.bottom){

                performClick()
                return true
            }

            return false
        }

        return false
    }

    fun disable(){
        textPaint.color = disabledTextColor
        adjustDrawables(disabledTextColor)
        isEnabled = false
        invalidate()
    }

    fun enable() {
        textPaint.color = textColor
        adjustDrawables(drawableTint)
        isEnabled = true
        requestLayout()
    }

    fun setBackgroundParams(@ColorInt color: Int, radiusDp: Int) {
        cornerRadius = dpToPixel(context, radiusDp)
        mBackgroundColor = color
        backgroundPaint.color = color
        shadowPaint.color = color
        lightPaint.color = color
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

    fun setDrawableParams(@DrawableRes drawableStart: Int?, @DrawableRes drawableEnd: Int?) {
        drawableStart?.let { this.drawableStart = it }
        drawableEnd?.let { this.drawableEnd = it }
        requestLayout()
    }

    fun setDrawableParams(@DrawableRes drawableStart: Int?, @DrawableRes drawableEnd: Int?, @ColorInt tint: Int?) {
        drawableStart?.let { this.drawableStart = it }
        drawableEnd?.let { this.drawableEnd = it }
        tint?.let { drawableTint = it }
        requestLayout()
    }

    fun setDrawableParams(@DrawableRes drawableStart: Int?, @DrawableRes drawableEnd: Int?, @ColorInt tint: Int?, horizontalPaddingDp: Int) {
        drawableStart?.let { this.drawableStart = it }
        drawableEnd?.let { this.drawableEnd = it }
        tint?.let { drawableTint = it }
        drawablePadding = dpToPixel(context, horizontalPaddingDp)
        requestLayout()
    }

    fun setDrawableDimension(dimensionDp: Int) {
        drawableDimension = dpToPixel(context, dimensionDp)
        requestLayout()
    }

    fun setTextPaddings(horizontalPaddingDp: Int, verticalPaddingDp: Int) {
        horizontalPadding = dpToPixel(context, horizontalPaddingDp)
        verticalPadding = dpToPixel(context, verticalPaddingDp)
    }

    fun setText(text: String, sizeDp: Int, @ColorInt color: Int) {
        this.text = text
        textSizeDp = dpToPixel(context, sizeDp)
        textColor = color
        requestLayout()
    }

    fun setText(text: String, sizeDp: Int) {
        this.text = text
        textSizeDp = dpToPixel(context, sizeDp)
        requestLayout()
    }

    fun setTypeface(style: Int, @FontRes font: Int) {
        textStyle = style
        textFont = font
        requestLayout()
    }

    fun setTypeface(@StyleRes style: Int) {
        textStyle = style
        requestLayout()
    }

    private fun adjustDrawables(tint: Int) {

        if(drawableStart != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableStart)!!
            if (tint != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(tint)
                }
            }
            drawableStartBitmap = drawable.toBitmap(drawableDimension.toInt(), drawableDimension.toInt(), Bitmap.Config.ARGB_8888)
            drawableStartX = shadowMargin.plus(drawablePadding)
        }

        if(drawableEnd != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableEnd)!!
            if (tint != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(tint)
                }
            }
            drawableEndBitmap = drawable.toBitmap(drawableDimension.toInt(), drawableDimension.toInt(), Bitmap.Config.ARGB_8888)
            drawableEndX = width
                .minus(shadowMargin)
                .minus(drawablePadding)
                .minus(drawableDimension)
        }

        drawablesY = height.div(2f).minus(drawableDimension.div(2))
    }

    private fun adjustText() {

        /**
         * Setting all text parameters before the text is drawn
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuButton.textSizeDp
            color = textColor
            textAlign = Paint.Align.CENTER
        }

        mTextX = width.div(2f).plus(textXOffSet) // textXOffSet is the offset caused by the drawables on either side
        val textVerticalMid = textPaint.descent().plus(textPaint.ascent()).div(2)
        mTextY = height.div(2f).minus(textVerticalMid)
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** Need to set the parameters that are determining in the measurement
         * of the length of the text since the size of the view is calculated
         * based on the height and the width of the text
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuButton.textSizeDp
        }
        textHeight = textPaint.descent().minus(textPaint.ascent())

        val width = textPaint.measureText(text)
            .plus(shadowMargin.times(2)) //the margin dedicated to the lighting and shadow from each side
            .plus(horizontalPadding.times(2)) //this padding applies only to the text increasing the width of the view
            .plus(getDrawablesCollectiveWidth())

        val height = textHeight
            .plus(shadowMargin.times(2))
            .plus(verticalPadding.times(2)) //this padding applies only to the text increasing the height of the view

        return NeuUtil.MinimumDimensions(width.toInt(), height.toInt())
    }

    private fun getDrawablesCollectiveWidth(): Float {

        /**
         * Calculating the amount of (collective) space needed for
         * one or both of the drawables to be drawn
         */

        var width = 0f
        textXOffSet = 0f

        if(drawableStart != 0){
            width = width.plus(drawableDimension).plus(drawablePadding.times(2))
            textXOffSet = textXOffSet
                .plus(drawableDimension) //to put offset the text to the right
                .plus(drawablePadding.times(2)) //Also to put offset the text to the right
                .minus(horizontalPadding.div(2)) // to put the text in the middle of the distance between the drawable and the end of the view
        }

        if(drawableEnd != 0){
            width = width.plus(drawableDimension).plus(drawablePadding.times(2))
            textXOffSet = textXOffSet
                .minus(drawableDimension)
                .minus(drawablePadding.times(2))
                .plus(horizontalPadding.div(2))
        }

        // The offset is divided by 2 to draw the text exactly
        // in the middle of the available space between the drawables
        textXOffSet = textXOffSet.div(2)

        return width
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
                radius = 25f
                lightOffset = 9f
                shadowOffset = 10f
            }

            Jut.LARGE -> {
                radius = 25f
                lightOffset = 10f
                shadowOffset = 11f
            }
        }

        lightPaint.apply {
            color = mBackgroundColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(mBackgroundColor, Color.WHITE, lightDensity))
        }

        shadowPaint.apply {
            color = mBackgroundColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(mBackgroundColor, Color.BLACK, shadowDensity))
        }
    }
}