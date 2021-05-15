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
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import com.smb.neumorphicviewset.interfaces.NeuUtil

class NeuImageButton : View, NeuUtil {

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
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private var mBackgroundColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
    private val backgroundRectF = RectF()
    private var cornerRadius: Float = dpToPixel(context, 8)
    private var horizontalPadding: Float = 0f
    private var verticalPadding: Float = 0f
    var disabledTextColor = Color.GRAY

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context,16)
    private var lightDensity: Float = 0.5f
    private var shadowDensity: Float = 0.5f
    private var jutSize: Int = 1
    private var jut: Jut = Jut.NORMAL

    /* Drawable Parameters */
    private lateinit var drawableBitmap: Bitmap
    private var drawableX: Float = 0f
    private var drawablesY: Float = 0f
    private var drawableDimension:Float = dpToPixel(context,25)
    private var drawable: Int = R.drawable.baseline_play_arrow_24
    private var drawableTint: Int = 0

    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {
        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuImageButton, defStyleAttr, 0)
        attrs.apply {
            cornerRadius = getDimension(R.styleable.NeuImageButton_nib_cornerRadius, cornerRadius)
            mBackgroundColor = getInteger(R.styleable.NeuImageButton_nib_backgroundColor, mBackgroundColor)

            drawableDimension = getDimension(R.styleable.NeuImageButton_nib_drawableDimension, drawableDimension)
            drawable = getResourceId(R.styleable.NeuImageButton_nib_drawable, drawable)
            drawableTint = getInteger(R.styleable.NeuImageButton_nib_drawableTint, drawableTint)

            lightDensity = getFloat(R.styleable.NeuImageButton_nib_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuImageButton_nib_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuImageButton_nib_JutSize, jutSize)

            horizontalPadding = getDimension(R.styleable.NeuImageButton_nib_HorizontalPadding, horizontalPadding)
            verticalPadding = getDimension(R.styleable.NeuImageButton_nib_VerticalPadding, verticalPadding)
            disabledTextColor = getInteger(R.styleable.NeuImageButton_nib_disabledColor, disabledTextColor)

            isEnabled = getBoolean(R.styleable.NeuImageButton_nib_enabled, true)

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

        backgroundRectF.set(shadowMargin, shadowMargin, width.minus(shadowMargin), height.minus(shadowMargin))
        backgroundPaint.color = mBackgroundColor

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, shadowPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)
            drawBitmap(drawableBitmap, drawableX, drawablesY, drawablePaint)
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
        adjustDrawables(disabledTextColor)
        isEnabled = false
        invalidate()
    }

    fun enable() {
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

    fun setDrawableParams(@DrawableRes drawable: Int, dimensionDp: Int) {
        this.drawable = drawable
        drawableDimension = dpToPixel(context, dimensionDp)
        requestLayout()
    }

    fun setDrawableParams(@DrawableRes drawable: Int, dimensionDp: Int, @ColorInt tint: Int) {
        this.drawable = drawable
        drawableTint = tint
        drawableDimension = dpToPixel(context, dimensionDp)
        requestLayout()
    }

    fun setPadding(horizontalPaddingDp: Int, verticalPaddingDp: Int) {
        horizontalPadding = dpToPixel(context, horizontalPaddingDp)
        verticalPadding = dpToPixel(context, verticalPaddingDp)
    }

    private fun adjustDrawables(tint: Int) {

        val drawable = ContextCompat.getDrawable(context, drawable)!!
        if (tint != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(tint)
            }
        }

        drawableBitmap = drawable.toBitmap(drawableDimension.toInt(), drawableDimension.toInt(), Bitmap.Config.ARGB_8888)

        drawableX = width.div(2).minus(drawableDimension.div(2))
        drawablesY = height.div(2f).minus(drawableDimension.div(2))
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** The size of the view is calculated based on
         * the height and the width of the drawable
         */

        val width = drawableDimension
            .plus(shadowMargin.times(2)) //the margin dedicated to the lighting and shadow from each side
            .plus(horizontalPadding.times(2))

        val height = drawableDimension
            .plus(shadowMargin.times(2))
            .plus(verticalPadding.times(2)) //this padding applies only to the text increasing the height of the view

        return NeuUtil.MinimumDimensions(width.toInt(), height.toInt())
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