package com.smb.neumorphicviewset

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap

class NeumorphicButton : View {

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

    private var cornerRadius: Float = dpToPixel(8)

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mBackgroundColor = ContextCompat.getColor(context, R.color.primaryColor)
    private val backgroundRectF = RectF()
    private var shadowMargin: Float = dpToPixel(16)

    private var lightDensity: Float = 0.5f
    private var shadowDensity: Float = 0.5f
    private var jutSize: Int = 1

    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var drawableStartBitmap: Bitmap
    private lateinit var drawableEndBitmap: Bitmap
    private var drawablePadding: Float = dpToPixel(8)
    private var drawableStartX: Float = 0f
    private var drawableEndX: Float = 0f
    private var drawableY: Float = 0f
    private var drawableDimension = dpToPixel(25).toInt()
    private var drawableStart: Int = 0
    private var drawableEnd: Int = 0
    @ColorInt
    var drawableTint: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private var textXOffSet: Float = 0f
    private var horizontalPadding: Float = dpToPixel(16)
    private var verticalPadding: Float = dpToPixel(16)
    private var touchX : Float = 0f
    private var touchY : Float = 0f
    private var textHeight: Float = 0f
    private var mTextColorOriginal: Int = Color.BLACK
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var mTextSize: Float = dpToPixel(16)
    private var mTextColorCurrent: Int = Color.BLACK
    var disabledTextColor = Color.GRAY

    var text: String = "Neumorphic Button"
        set(value) {
            field = value
            requestLayout()
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


    private var viewJut: Jut = Jut.NORMAL


    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeumorphicButton, defStyleAttr, 0)

        attrs.apply {

            cornerRadius = getDimension(R.styleable.NeumorphicButton_neu_cornerRadius, cornerRadius)
            mBackgroundColor = getInteger(R.styleable.NeumorphicButton_neu_backgroundColor, mBackgroundColor)

            //retrieving drawable attributes
            drawableStart = getResourceId(R.styleable.NeumorphicButton_neu_drawableStart, 0)
            drawableEnd = getResourceId(R.styleable.NeumorphicButton_neu_drawableEnd, 0)
            drawablePadding = getDimension(R.styleable.NeumorphicButton_neu_drawablePadding, drawablePadding)
            drawableTint = getInteger(R.styleable.NeumorphicButton_neu_drawableTint, 0)

            lightDensity = getFloat(R.styleable.NeumorphicButton_neu_lightDensity, lightDensity)
            shadowDensity = getFloat(R.styleable.NeumorphicButton_neu_shadowDensity, shadowDensity)
            jutSize = getInt(R.styleable.NeumorphicButton_neu_JutSize, jutSize)


            //retrieving text attributes
            horizontalPadding = getDimension(R.styleable.NeumorphicButton_neu_HorizontalPadding, horizontalPadding)
            verticalPadding = getDimension(R.styleable.NeumorphicButton_neu_VerticalPadding, verticalPadding)
            textStyle = getInt(R.styleable.NeumorphicButton_neu_textStyle, textStyle)
            mTextSize = getDimension(R.styleable.NeumorphicButton_neu_textSize, mTextSize)
            mTextColorCurrent = getInteger(R.styleable.NeumorphicButton_neu_textColor, mTextColorCurrent)
            textFont = getResourceId(R.styleable.NeumorphicButton_neu_fontFamily, 0)
            disabledTextColor = getInteger(R.styleable.NeumorphicButton_neu_disabledTextColor, disabledTextColor)
            mTextColorOriginal = mTextColorCurrent

            text = getString(R.styleable.NeumorphicButton_neu_text) ?: text

            recycle()
        }

        when (jutSize) {
            0 -> viewJut = Jut.SMALL
            1 -> viewJut = Jut.NORMAL
            3 -> viewJut = Jut.LARGE
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

        adjustDrawables()
        setJutSize(viewJut)
        adjustText()

        backgroundRectF.set(shadowMargin, shadowMargin, width.minus(shadowMargin), height.minus(shadowMargin))
        backgroundPaint.color = mBackgroundColor


        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, shadowPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)
            if (drawableStart != 0) {
                drawBitmap(drawableStartBitmap, drawableStartX, drawableY, drawablePaint)
            }
            if (drawableEnd != 0) {
                drawBitmap(drawableEndBitmap, drawableEndX, drawableY, drawablePaint)
            }
            drawText(text, mTextX, mTextY, textPaint)
        }
    }

    private fun adjustDrawables() {
        if(drawableStart != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableStart)!!
            if (drawableTint != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(drawableTint)
                }
            }
            drawableStartBitmap = drawable.toBitmap(drawableDimension, drawableDimension, Bitmap.Config.ARGB_8888)
            drawableStartX = shadowMargin + drawablePadding
        }

        if(drawableEnd != 0) {
            val drawable = ContextCompat.getDrawable(context, drawableEnd)!!
            if (drawableTint != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTint(drawableTint)
                }
            }
            drawableEndBitmap = drawable.toBitmap(drawableDimension, drawableDimension, Bitmap.Config.ARGB_8888)
            drawableEndX = width - shadowMargin - drawablePadding - drawableDimension
        }

        drawableY = height.div(2f).minus(drawableDimension.div(2))
    }

    private fun adjustText() {

        textPaint.apply {
            val tf = getTypeFace()
            typeface = Typeface.create(tf, textStyle)
            textSize = mTextSize
            color = mTextColorCurrent
            textAlign = Paint.Align.CENTER
        }

        mTextX = width.div(2f) + textXOffSet
        mTextY = height.div(2f) - textPaint.descent().plus(textPaint.ascent()).div(2)
    }

    private fun getDesiredDimensions(): MinimumDimensions {

        textPaint.apply {
            val tf = getTypeFace()
            typeface = Typeface.create(tf, textStyle)
            textSize = mTextSize
        }
        textHeight = textPaint.descent().minus(textPaint.ascent())

        val width = textPaint.measureText(text) + shadowMargin.times(2) +
                horizontalPadding.times(2) + getDrawablesCollectiveWidth()
        val height = textHeight + shadowMargin.times(2) + verticalPadding.times(2)

        return MinimumDimensions(width.toInt(), height.toInt())
    }

    private fun getFinalDimension(desiredDimen: Int, measureSpec: Int): Int {

        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        return when(mode){
            MeasureSpec.EXACTLY -> {
                size
            }
            MeasureSpec.AT_MOST -> {
                desiredDimen.coerceAtMost(size)
            }
            else -> {
                desiredDimen
            }
        }
    }

    private fun getDrawablesCollectiveWidth(): Float {

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

        textXOffSet = textXOffSet.div(2)
        return width
    }

    private fun setJutSize(jut: Jut) {

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

    private fun dpToPixel(dp: Int): Float {
        return dp.times(resources.displayMetrics.density)
    }

    private fun getTypeFace(): Typeface {
        var tf = Typeface.DEFAULT
        if(textFont != 0){
            tf = ResourcesCompat.getFont(context, textFont)
        }

        return tf
    }

    private data class MinimumDimensions(val width: Int, val height: Int)
}