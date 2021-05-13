package com.smb.neumorphicviewset

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.smb.neumorphicviewset.interfaces.NeuUtil

class NeuRadioButton : View, NeuUtil {

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
    private val radioButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val checkMarkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private val radioButtonRectF = RectF()
    var radioButtonColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
        set(value) {
            field = value
            invalidate()
        }

    /* Check box parameters */
    private var checkMarkStrokeWidth: Float = dpToPixel(context, 8)
    private var checkMarkColor: Int = Color.CYAN
    private val checkMarkGlowRadius = 25f
    var isChecked: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    private var buttonRadius: Float = dpToPixel(context, 12)
    private val handleGlowRadius = 25f
    private var checkMarkRadius = buttonRadius.minus(checkMarkStrokeWidth.div(2))
    private var handleX: Float = 0f
    private var handleY: Float = 0f


    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context, 12)
    private var lightDensity: Float = 0.4f
    private var shadowDensity: Float = 0.6f
    private var jutSize: Int = 1
    private var jut: Jut = Jut.NORMAL

    /* Text parameters */
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
    private var textHeight: Float = 0f
    private var textSize: Float = dpToPixel(context, 16)
    private var textColor: Int = Color.BLACK
    private var textStyle: Int = Typeface.NORMAL
    private var textFont: Int = 0
    var disabledColor = Color.GRAY
    var text: String = ""
        set(value) {
            field = value
            requestLayout()
        }

//    private var onNeuCheckedChangeListener: OnNeuCheckBoxCheckedChangeListener? = null
//
//    fun setOnNeuCheckedChangeListener(onCheckedChangeListener: OnNeuCheckBoxCheckedChangeListener) {
//        this.onNeuCheckedChangeListener = onCheckedChangeListener
//    }

    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuRadioButton, defStyleAttr, 0)
        attrs.apply {
            radioButtonColor = getInteger(R.styleable.NeuRadioButton_nrb_backgroundColor, radioButtonColor)

            lightDensity = getFloat(R.styleable.NeuRadioButton_nrb_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuRadioButton_nrb_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuRadioButton_nrb_JutSize, jutSize)

            textStyle = getInt(R.styleable.NeuRadioButton_nrb_textStyle, textStyle)
            textSize = getDimension(R.styleable.NeuRadioButton_nrb_textSize, textSize)
            textColor = getInteger(R.styleable.NeuRadioButton_nrb_textColor, textColor)
            textFont = getResourceId(R.styleable.NeuRadioButton_nrb_fontFamily, 0)
            disabledColor = getInteger(R.styleable.NeuRadioButton_nrb_disabledColor, disabledColor)
            text = getString(R.styleable.NeuRadioButton_nrb_text) ?: text

            isChecked = getBoolean(R.styleable.NeuRadioButton_nrb_checked, isChecked)
            isEnabled = getBoolean(R.styleable.NeuRadioButton_nrb_enabled, isEnabled)

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

        adjustJutParams(jut)
        adjustCheckBoxParams()
        adjustText()

        if (!isEnabled) {
//            disable()
        }

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {

            drawCircle(handleX, handleY, buttonRadius, lightPaint)
            drawCircle(handleX, handleY, buttonRadius, radioButtonPaint)
            drawText(text, mTextX, mTextY, textPaint)

            if (isChecked) {
                drawCircle(handleX, handleY, checkMarkRadius, checkMarkPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event?.action == MotionEvent.ACTION_DOWN && isEnabled){

            isChecked = !isChecked
            invalidate()

//            onNeuCheckedChangeListener?.onCheckedChanged(this, CheckedStatus)

            performClick()
            return true
        }

        return false
    }
/*

    fun enable() {
        isEnabled = true
        textPaint.color = textColor
        checkMarkPaint.apply {
            color = checkMarkColor
            setShadowLayer(checkMarkGlowRadius, 0f, 0f, checkMarkColor)
        }

        invalidate()
    }

    fun disable() {
        isEnabled = false
        textPaint.color = disabledColor
        checkMarkPaint.apply {
            color = disabledColor
            setShadowLayer(checkMarkGlowRadius, 0f, 0f, disabledColor)
        }

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

    fun setCheckBoxDimension(dimensionDp: Int) {
        checkBoxDimension = dpToPixel(context, dimensionDp)
        requestLayout()
    }

    fun setCheckMarkParams(@ColorInt color: Int) {
        checkMarkColor = color
        invalidate()
    }

    fun setCheckMarkParams(@ColorInt color: Int, strokeWidthDp: Int) {
        checkMarkStrokeWidth = dpToPixel(context, strokeWidthDp)
        checkMarkColor = color
        invalidate()
    }

    fun setText(text: String, sizeDp: Int, @ColorInt color: Int) {
        this.text = text
        textSize = dpToPixel(context, sizeDp)
        textColor = color
        requestLayout()
    }

    fun setText(text: String, sizeDp: Int) {
        this.text = text
        textSize = dpToPixel(context, sizeDp)
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

*/
    private fun adjustText() {

        /**
         * Setting all text parameters before the text is drawn
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuRadioButton.textSize
            color = textColor
        }

        mTextX = buttonRadius.times(2).plus(shadowMargin.times(2)).plus(checkMarkStrokeWidth)
        val textVerticalMid = textPaint.descent().plus(textPaint.ascent()).div(2)
        mTextY = height.div(2f).minus(textVerticalMid)
    }

    private fun adjustCheckBoxParams() {
        handleX = shadowMargin.plus(buttonRadius).plus(checkMarkStrokeWidth.div(2))
        handleY = height.div(2f)

        radioButtonPaint.color = radioButtonColor
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** Need to set the parameters that are determining in the measurement
         * of the length of the text since the size of the view is calculated
         * based on the height and the width of the text
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuRadioButton.textSize
        }
        val textHeight = textPaint.descent().minus(textPaint.ascent())

        val radioButtonDimen = buttonRadius.times(2).plus(checkMarkStrokeWidth).plus(shadowMargin.times(2))

        var width = textPaint.measureText(text).plus(radioButtonDimen)
        if (text.isNotBlank()) {
            width = width.plus(shadowMargin)
        }

        val height = textHeight.coerceAtLeast(radioButtonDimen)

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
                shadowOffset = 11f
            }

            Jut.LARGE -> {
                radius = 25f
                lightOffset = 10f
                shadowOffset = 11f
            }
        }

        lightPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = checkMarkStrokeWidth
            color = radioButtonColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(radioButtonColor, Color.WHITE, lightDensity))
        }

        radioButtonPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = checkMarkStrokeWidth
            color = radioButtonColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(radioButtonColor, Color.BLACK, shadowDensity))
        }

        checkMarkPaint.apply {
            color = checkMarkColor
            setShadowLayer(handleGlowRadius, 0f, 0f, checkMarkColor)
        }
    }
}