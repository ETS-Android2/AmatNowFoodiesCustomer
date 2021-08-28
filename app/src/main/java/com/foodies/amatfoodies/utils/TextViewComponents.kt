package com.foodies.amatfoodies.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.foodies.amatfoodies.utils.FontCache.getTypeface

open class CustomTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        applyCustomFont(context)
    }

    private fun applyCustomFont(context: Context) {
        val customFont = getTypeface("Manrope/Manrope-Medium.ttf", context)
        setTypeface(customFont, Typeface.NORMAL)
    }
}

class FormLabelTextView : CustomTextView {
    constructor(context: Context) : super(context) {
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        setOptions(context)
    }

    private fun setOptions(context: Context) {
        textSize = 15F
        lineHeight = 19
//        setTextColor(context.resources.getColor(R.color.label_color))
    }
}