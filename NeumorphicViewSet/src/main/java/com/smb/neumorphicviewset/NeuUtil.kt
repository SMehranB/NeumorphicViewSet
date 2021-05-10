package com.smb.neumorphicviewset

import android.content.Context
import android.graphics.Typeface
import android.view.View
import androidx.core.content.res.ResourcesCompat

interface NeuUtil {

    fun getFinalDimension(desiredDimen: Int, measureSpec: Int): Int {

        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)

        return when(mode){
            View.MeasureSpec.EXACTLY -> {
                size
            }
            View.MeasureSpec.AT_MOST -> {
                desiredDimen.coerceAtMost(size)
            }
            else -> {
                desiredDimen
            }
        }
    }

    fun getTypeFace(context: Context, font: Int, textStyle: Int): Typeface {
        var tf = Typeface.DEFAULT
        if(font != 0){
            tf = ResourcesCompat.getFont(context, font)
        }

        return Typeface.create(tf, textStyle)
    }

    fun dpToPixel(context: Context, dp: Int): Float {
        return dp.times(context.resources.displayMetrics.density)
    }

    data class MinimumDimensions(val width: Int, val height: Int)
}