package com.smb.neumorphicviewset

import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.smb.neumorphicviewset.interfaces.NeuUtil

class NeuCheckBox : View, NeuUtil {
    constructor(context: Context): super(context){
        initAttributes(context, null, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        initAttributes(context, attributeSet, 0)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        initAttributes(context, attributeSet, defStyleAttr)
    }


    private val cornerRadius: Float = dpToPixel(context, 5)
    private var checkBoxDimension: Float = dpToPixel(context, 24)
    private var cbStrokeWidth: Float = dpToPixel(context, 5)
    private var isChecked: Boolean = false
    private var animatorSet: AnimatorSet? = null

    /* Paint objects */
    private val checkBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private var mBackgroundColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
    private val checkBoxRectF = RectF()

    /* Shadow and lighting parameters */
    private var shadowMargin: Float = dpToPixel(context, 16)
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
    var disabledTextColor = Color.GRAY
    var text: String = "Neu Check Box"
        set(value) {
            field = value
            requestLayout()
        }


    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuCheckBox, defStyleAttr, 0)
        attrs.apply {
            mBackgroundColor = getInteger(R.styleable.NeuCheckBox_ncb_backgroundColor, mBackgroundColor)

            lightDensity = getFloat(R.styleable.NeuCheckBox_ncb_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuCheckBox_ncb_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuCheckBox_ncb_JutSize, jutSize)

            textStyle = getInt(R.styleable.NeuCheckBox_ncb_textStyle, textStyle)
            textSize = getDimension(R.styleable.NeuCheckBox_ncb_textSize, textSize)
            textColor = getInteger(R.styleable.NeuCheckBox_ncb_textColor, textColor)
            textFont = getResourceId(R.styleable.NeuCheckBox_ncb_fontFamily, 0)
            disabledTextColor = getInteger(R.styleable.NeuCheckBox_ncb_disabledColor, disabledTextColor)
            text = getString(R.styleable.NeuCheckBox_ncb_text) ?: text

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

        adjustJutParams(jut)
        adjustCheckBoxParams()
        adjustText()

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawRoundRect(checkBoxRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(checkBoxRectF, cornerRadius, cornerRadius, checkBoxPaint)
            drawText(text, mTextX, mTextY, textPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event?.action == MotionEvent.ACTION_DOWN && isEnabled){

            if(event.x in checkBoxRectF.left..checkBoxRectF.right &&
                event.y in checkBoxRectF.top..checkBoxRectF.bottom){

                performClick()
                return true
            }

            return false
        }

        return false
    }


    private fun adjustText() {

        /**
         * Setting all text parameters before the text is drawn
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuCheckBox.textSize
            color = textColor
        }

        mTextX = checkBoxRectF.right.plus(shadowMargin)
        val textVerticalMid = textPaint.descent().plus(textPaint.ascent()).div(2)
        mTextY = height.div(2f).minus(textVerticalMid)
    }

    private fun adjustCheckBoxParams() {

        val left = shadowMargin.plus(cbStrokeWidth.div(2))
        val right = left.plus(checkBoxDimension)
        val top = shadowMargin.plus(cbStrokeWidth.div(2))
        val bottom = top.plus(checkBoxDimension)

        checkBoxRectF.set(left, top, right, bottom)
        checkBoxPaint.color = mBackgroundColor
    }
    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** Need to set the parameters that are determining in the measurement
         * of the length of the text since the size of the view is calculated
         * based on the height and the width of the text
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuCheckBox.textSize
        }
        textHeight = textPaint.descent().minus(textPaint.ascent())

        val checkBoxDimen = checkBoxDimension.plus(cbStrokeWidth).plus(shadowMargin.times(2))

        var width = textPaint.measureText(text)
            .plus(checkBoxDimen)

        if (text.isNotBlank()) {
            width = width.plus(shadowMargin)
        }

        val height = textHeight.coerceAtLeast(checkBoxDimen)

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
            color = mBackgroundColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(mBackgroundColor, Color.WHITE, lightDensity))
        }

        checkBoxPaint.apply {
            color = mBackgroundColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(mBackgroundColor, Color.BLACK, shadowDensity))
        }
    }
}