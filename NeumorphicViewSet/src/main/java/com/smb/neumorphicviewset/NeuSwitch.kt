package com.smb.neumorphicviewset

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.smb.neumorphicviewset.interfaces.MyAnimatorListener
import com.smb.neumorphicviewset.interfaces.NeuUtil

class NeuSwitch: View, NeuUtil {
    constructor(context: Context): super(context){
        initAttributes(context, null, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        initAttributes(context, attributeSet, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        initAttributes(context, attributeSet, defStyleAttr)
    }

    private var isChecked: Boolean = false
    private var animatorSet: AnimatorSet? = null

    /* Paint objects */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val handlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Handle Parameters */
    private var handleMargin: Float = dpToPixel(context, 5)
    private var handleX: Float = 0f
    private var handleY: Float = 0f
    private var handleRadius: Float = dpToPixel(context, 12)
    private val handleGlowRadius = 25f
    var handleColor: Int = Color.CYAN
        set(value) {
            field = value
            handleOffColor = ColorUtils.blendARGB(Color.BLACK, handleColor, 0.4f)
            handlePaint.color = if (isChecked) {
                handlePaint.setShadowLayer(handleGlowRadius, 0f, 0f, handleColor)
                handleColor
            }else {
                handleOffColor
            }
            invalidate()
        }
    private var handleOffColor: Int = ColorUtils.blendARGB(Color.BLACK, handleColor, 0.4f)

    /* Background parameters */
    private var switchHeight = dpToPixel(context, 30)
    private var switchWidth = dpToPixel(context, 50)
    var switchColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
        set(value) {
            field = value
            backgroundPaint.color = value
            invalidate()
        }
    private val backgroundRectF = RectF()
    private var cornerRadius: Float = dpToPixel(context, 100)
    var disabledColor = Color.GRAY

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context,12)
    private var lightDensity: Float = 0.5f
    private var shadowDensity: Float = 0.5f
    private var jutSize: Int = 1
    private var jut: Jut = Jut.NORMAL

    /* Text parameters */
    private var mTextX: Float = 0f
    private var mTextY: Float = 0f
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
    var text: String = ""
        set(value) {
            field = value
            requestLayout()
        }

    private var onCheckedChangeListener: OnCheckedChangeListener? = null


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
        adjustText()
        adjustJutParams(jut)

        setLayerTypeBasedOnSDK(this, lightPaint)

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.apply {
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(backgroundRectF, cornerRadius, cornerRadius, backgroundPaint)
            drawCircle(handleX, handleY, handleRadius, handlePaint)
            drawText(text, mTextX, mTextY, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null && isEnabled && event.action == MotionEvent.ACTION_DOWN) {

            if (isChecked) {
                turnOff()
            } else {
                turnOn()
            }

            isChecked = !isChecked

            performClick()
            return true
        }

        return false
    }

    fun setChecked(checked: Boolean) {
        if (checked && !isChecked) {
            turnOn()
        } else if (!checked && isChecked) {
            turnOff()
        }
    }

    fun enable() {
        isEnabled = true
        handlePaint.apply {
            if (isChecked) {
                color = handleColor
                setShadowLayer(handleGlowRadius, 0f, 0f, handleColor)
            } else {
                color = handleOffColor
            }
        }
        invalidate()
    }

    fun disable() {
        isEnabled = false
        handlePaint.apply {
            color = disabledColor
            if (isChecked) {
                setShadowLayer(handleGlowRadius, 0f, 0f, disabledColor)
            }
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

    fun setOnNeuCheckedChangeListener(listener: OnCheckedChangeListener) {
        onCheckedChangeListener = listener
    }

    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuSwitch, defStyleAttr, 0)
        attrs.apply {
            switchColor = getInteger(R.styleable.NeuSwitch_ns_backgroundColor, switchColor)
            switchWidth = getDimension(R.styleable.NeuSwitch_ns_SwitchWidth, switchWidth)
            switchHeight = getDimension(R.styleable.NeuSwitch_ns_SwitchHeight, switchHeight)

            lightDensity = getFloat(R.styleable.NeuSwitch_ns_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuSwitch_ns_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuSwitch_ns_JutSize, jutSize)

            textStyle = getInt(R.styleable.NeuSwitch_ns_textStyle, textStyle)
            textSizeDp = getDimension(R.styleable.NeuSwitch_ns_textSize, textSizeDp)
            textColor = getInteger(R.styleable.NeuSwitch_ns_textColor, textColor)
            textFont = getResourceId(R.styleable.NeuSwitch_ns_fontFamily, 0)
            text = getString(R.styleable.NeuSwitch_ns_text) ?: text

            disabledColor = getInteger(R.styleable.NeuSwitch_ns_disabledColor, disabledColor)
            handleColor = getInt(R.styleable.NeuSwitch_ns_handleColor, handleColor)

            isChecked = getBoolean(R.styleable.NeuSwitch_ns_checked, isChecked)
            isEnabled = getBoolean(R.styleable.NeuSwitch_ns_enabled, isEnabled)

            recycle()
        }

        when (jutSize) {
            0 -> jut = Jut.SMALL
            1 -> jut = Jut.NORMAL
            2 -> jut = Jut.LARGE
        }
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuSwitch.textSizeDp
        }
        val textHeight = textPaint.descent().minus(textPaint.ascent())

        val switchWidth = switchWidth.plus(shadowMargin.times(2))
        val switchHeight = switchHeight.plus(shadowMargin.times(2))

        var width = textPaint.measureText(text).plus(switchWidth)
        if (text.isNotBlank()) {
            width = width.plus(shadowMargin)
        }

        val height = textHeight.coerceAtLeast(switchHeight)

        return NeuUtil.MinimumDimensions(width.toInt(), height.toInt())
    }

    private fun turnOn() {

        animatorSet?.cancel()

        val slideRight = ValueAnimator.ofFloat(handleX, getCheckedX())
        slideRight.addUpdateListener {
            handleX = it.animatedValue as Float
        }

        val colorChange = ValueAnimator.ofFloat(0f, 1f)
        colorChange.addUpdateListener {
            val value = it.animatedValue as Float
            val color = ColorUtils.blendARGB(handleOffColor, handleColor, value)
            handlePaint.color = color
            handlePaint.setShadowLayer(value.times(handleGlowRadius), 0f, 0f, color)
            invalidate()
        }

        animatorSet = AnimatorSet()
        animatorSet!!.apply {
            addListener(object: MyAnimatorListener{
                override fun onAnimationEnd(p0: Animator?) {
                    onCheckedChangeListener?.onCheckedChanged(this@NeuSwitch, true)
                }
            })
            playTogether(slideRight, colorChange)
            start()
        }
    }

    private fun turnOff() {

        animatorSet?.cancel()

        val slideRight = ValueAnimator.ofFloat(handleX, getUnCheckedX())
        slideRight.addUpdateListener {
            handleX = it.animatedValue as Float
        }

        val colorChange = ValueAnimator.ofFloat(1f, 0f)
        colorChange.addUpdateListener {
            val value = it.animatedValue as Float
            val color = ColorUtils.blendARGB(handleOffColor, handleColor, value)
            handlePaint.color = color
            handlePaint.setShadowLayer(value.times(handleGlowRadius), 0f, 0f, color)
            invalidate()
        }

        animatorSet = AnimatorSet()
        animatorSet!!.apply {
            addListener(object: MyAnimatorListener{
                override fun onAnimationEnd(p0: Animator?) {
                    onCheckedChangeListener?.onCheckedChanged(this@NeuSwitch, false)
                }
            })
            playTogether(slideRight, colorChange)
            start()
        }
    }

    private fun adjustText() {

        /**
         * Setting all text parameters before the text is drawn
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuSwitch.textSizeDp
            color = textColor
        }

        mTextX = backgroundRectF.right.plus(shadowMargin)
        val textVerticalMid = textPaint.descent().plus(textPaint.ascent()).div(2)
        mTextY = height.div(2f).minus(textVerticalMid)
    }

    private fun adjustBackgroundParams() {
        val left = shadowMargin
        val right = left.plus(switchWidth)
        val top = height.div(2).minus(switchHeight.div(2))
        val bottom = top.plus(switchHeight)

        backgroundRectF.set(left, top, right, bottom)
        cornerRadius = backgroundRectF.height().div(2)
        backgroundPaint.apply {
            style = Paint.Style.FILL
            color = switchColor
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
                radius = 25f
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
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(switchColor, Color.BLACK, shadowDensity))
        }

        lightPaint.apply {
            color = switchColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(switchColor, Color.WHITE, lightDensity))
        }

        handleRadius =  backgroundRectF.height().div(2).minus(handleMargin)
        handleX = getUnCheckedX()
        handleY = height.div(2f)
        handlePaint.apply {
            color = if (isEnabled) {
                if (isChecked) {
                    handleX = getCheckedX()
                    setShadowLayer(handleGlowRadius, 0f, 0f, handleColor)
                    handleColor
                } else {
                    handleOffColor
                }
            } else {
                if (isChecked) {
                    handleX = getCheckedX()
                    setShadowLayer(handleGlowRadius, 0f, 0f, disabledColor)
                }
                disabledColor
            }
        }
    }

    private fun getCheckedX(): Float {
        return backgroundRectF.right.minus(handleRadius).minus(handleMargin)
    }

    private fun getUnCheckedX(): Float {
        return backgroundRectF.left.plus(handleRadius).plus(handleMargin)
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(neuSwitch: NeuSwitch, checked: Boolean)
    }
}