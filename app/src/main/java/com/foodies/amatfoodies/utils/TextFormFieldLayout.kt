package com.foodies.amatfoodies.utils

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.DarkModePrefManager
import com.foodies.amatfoodies.utils.FontCache.getTypeface
import com.google.android.material.textfield.TextInputLayout

class TextFormFieldLayout : TextInputLayout {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
        setOptions()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
        setOptions()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        applyCustomFont(context)
        setOptions()
    }

    private fun applyCustomFont(context: Context) {
        val customFont = getTypeface("Manrope/Manrope-Medium.ttf", context)
        typeface = customFont
    }

    private fun setOptions() {
        isHintEnabled = false
        isHintAnimationEnabled = false
        isErrorEnabled = true
        setErrorIconTintMode(PorterDuff.Mode.CLEAR)
        if (!DarkModePrefManager(context).isNightMode) {
            boxBackgroundColor =
                ResourcesCompat.getColor(resources, R.color.colorLightBG, context.theme)
        } else {
            boxBackgroundColor =
                ResourcesCompat.getColor(resources, R.color.quantum_grey900, context.theme)
        }
    }
}