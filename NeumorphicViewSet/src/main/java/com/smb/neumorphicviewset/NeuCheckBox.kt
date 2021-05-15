package com.smb.neumorphicviewset

import android.annotation.SuppressLint
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

    /* Paint objects */
    private val checkBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val checkMarkPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val lightPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /* Background parameters */
    private val checkBoxRectF = RectF()
    var checkBoxColor = ContextCompat.getColor(context, R.color.neuPrimaryColor)
        set(value) {
            field = value
            invalidate()
        }

    /* Check box parameters */
    private val cornerRadius: Float = dpToPixel(context, 5)
    private var checkBoxDimension: Float = dpToPixel(context, 24)
    private var checkMarkStrokeWidth: Float = dpToPixel(context, 3)
    private var checkMarkPath: Path = Path()
    private var checkMarkColor: Int = Color.CYAN
    private val checkMarkGlowRadius = 25f
    var isChecked: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

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
    var disabledColor = Color.GRAY
    var text: String = ""
        set(value) {
            field = value
            requestLayout()
        }


private var onNeuCheckedChangeListener: OnCheckedChangeListener? = null


    fun setOnNeuCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.onNeuCheckedChangeListener = listener
    }

    private fun initAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) {

        val attrs = context.theme.obtainStyledAttributes(attributeSet, R.styleable.NeuCheckBox, defStyleAttr, 0)
        attrs.apply {
            checkBoxColor = getInteger(R.styleable.NeuCheckBox_ncb_CheckBoxColor, checkBoxColor)

            lightDensity = getFloat(R.styleable.NeuCheckBox_ncb_lightDensity, lightDensity).coerceAtMost(1f)
            shadowDensity = getFloat(R.styleable.NeuCheckBox_ncb_shadowDensity, shadowDensity).coerceAtMost(1f)
            jutSize = getInt(R.styleable.NeuCheckBox_ncb_JutSize, jutSize)

            checkMarkColor = getInt(R.styleable.NeuCheckBox_ncb_CheckMarkColor, checkMarkColor)
            checkMarkStrokeWidth = getDimension(R.styleable.NeuCheckBox_ncb_CheckMarkStrokeWidth, checkMarkStrokeWidth)
            checkBoxDimension = getDimension(R.styleable.NeuCheckBox_ncb_CheckBoxDimension, checkBoxDimension)

            textStyle = getInt(R.styleable.NeuCheckBox_ncb_textStyle, textStyle)
            textSizeDp = getDimension(R.styleable.NeuCheckBox_ncb_textSize, textSizeDp)
            textColor = getInteger(R.styleable.NeuCheckBox_ncb_textColor, textColor)
            textFont = getResourceId(R.styleable.NeuCheckBox_ncb_fontFamily, 0)
            disabledColor = getInteger(R.styleable.NeuCheckBox_ncb_disabledColor, disabledColor)
            text = getString(R.styleable.NeuCheckBox_ncb_text) ?: text

            isChecked = getBoolean(R.styleable.NeuCheckBox_ncb_checked, isChecked)
            isEnabled = getBoolean(R.styleable.NeuCheckBox_ncb_enabled, isEnabled)

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
            disable()
        }

        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawRoundRect(checkBoxRectF, cornerRadius, cornerRadius, lightPaint)
            drawRoundRect(checkBoxRectF, cornerRadius, cornerRadius, checkBoxPaint)
            drawText(text, mTextX, mTextY, textPaint)

            if (isChecked) {
                drawPath(checkMarkPath, checkMarkPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if(event?.action == MotionEvent.ACTION_DOWN && isEnabled){

            isChecked = !isChecked
            invalidate()

            onNeuCheckedChangeListener?.onCheckedChanged(this, isChecked)

            performClick()
            return true
        }

        return false
    }

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
        checkMarkPaint.apply {
            this.color = color
            setShadowLayer(checkMarkGlowRadius, 0f, 0f, checkMarkColor)
        }
        invalidate()
    }

    fun setCheckMarkParams(@ColorInt color: Int, strokeWidthDp: Int) {
        checkMarkStrokeWidth = dpToPixel(context, strokeWidthDp)
        checkMarkColor = color
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

    private fun adjustText() {

        /**
         * Setting all text parameters before the text is drawn
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuCheckBox.textSizeDp
            color = textColor
        }

        mTextX = checkBoxRectF.right.plus(shadowMargin)
        val textVerticalMid = textPaint.descent().plus(textPaint.ascent()).div(2)
        mTextY = height.div(2f).minus(textVerticalMid)
    }

    private fun adjustCheckBoxParams() {

        val left = shadowMargin
        val right = left.plus(checkBoxDimension)
        val top = height.div(2).minus(checkBoxDimension.div(2))
        val bottom = top.plus(checkBoxDimension)

        checkBoxRectF.set(left, top, right, bottom)
        checkBoxPaint.color = checkBoxColor

        checkMarkPath = getCheckMarkPath()
    }

    private fun getDesiredDimensions(): NeuUtil.MinimumDimensions {

        /** Need to set the parameters that are determining in the measurement
         * of the length of the text since the size of the view is calculated
         * based on the height and the width of the text
         */

        textPaint.apply {
            typeface = getTypeFace(context, textFont, textStyle)
            textSize = this@NeuCheckBox.textSizeDp
        }
        textHeight = textPaint.descent().minus(textPaint.ascent())

        val checkBoxDimen = checkBoxDimension.plus(checkMarkStrokeWidth).plus(shadowMargin.times(2))

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
            color = checkBoxColor
            setShadowLayer(radius, -lightOffset, -lightOffset, ColorUtils.blendARGB(checkBoxColor, Color.WHITE, lightDensity))
        }

        checkBoxPaint.apply {
            color = checkBoxColor
            setShadowLayer(radius, shadowOffset, shadowOffset, ColorUtils.blendARGB(checkBoxColor, Color.BLACK, shadowDensity))
        }

        checkMarkPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = checkMarkStrokeWidth
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            color = checkMarkColor
            setShadowLayer(checkMarkGlowRadius, 0f, 0f, checkMarkColor)
        }
    }

    private fun getCheckMarkPath(): Path {

        val path = Path()

        val segment = checkBoxDimension.div(4)
        val startX = checkBoxRectF.left.plus(segment)
        val startY = checkBoxRectF.top.plus(segment.times(2))
        val midX = startX.plus(segment.div(2))
        val midY = startY.plus(segment)
        val endX = midX.plus(segment.times(1.5f))
        val endY = midY.minus(segment.times(2))

        path.moveTo(startX, startY)
        path.lineTo(midX, midY)
        path.lineTo(endX, endY)

        return path
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(neuCheckBox: NeuCheckBox, checked: Boolean)
    }
}