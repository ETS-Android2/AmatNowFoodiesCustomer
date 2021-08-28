package com.foodies.amatfoodies.utils

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.airbnb.paris.extensions.style
import com.foodies.amatfoodies.R
import com.foodies.amatfoodies.constants.DarkModePrefManager
import com.google.android.material.button.MaterialButton

open class FlatButton : MaterialButton {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        applyCustomFont(context)
        setOptions(context)
    }

    private fun applyCustomFont(context: Context) {
        val customFont = FontCache.getTypeface("Manrope/Manrope-Medium.ttf", context)
        typeface = customFont
    }

    private fun setOptions(context: Context) {
        textSize = 16F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) lineHeight = 24
        setTextColor(ResourcesCompat.getColor(resources, R.color.white, context.theme))
        elevation = 0F
        gravity = Gravity.CENTER
        backgroundTintList = null
        background = AppCompatResources.getDrawable(context, R.drawable.flat_button_background)
        rippleColor = AppCompatResources.getColorStateList(context, R.color.whiteSmoke)
    }
}

class TransparentButton : MaterialButton {
    constructor(context: Context) : super(context) {
        applyCustomFont(context)
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        applyCustomFont(context)
        setOptions(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        applyCustomFont(context)
        setOptions(context)
    }

    private fun applyCustomFont(context: Context) {
        val customFont = FontCache.getTypeface("Manrope/Manrope-Medium.ttf", context)
        typeface = customFont
    }

    private fun setOptions(context: Context) {
        textSize = 16F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) lineHeight = 24
        setTextColor(ResourcesCompat.getColor(resources, R.color.colorBlack, context.theme))
        elevation = 1F
        gravity = Gravity.CENTER
        cornerRadius = 8
        backgroundTintList =
            ResourcesCompat.getColorStateList(resources, R.color.colorWhite, context.theme)

        if (!DarkModePrefManager(context).isNightMode) {
            rippleColor = AppCompatResources.getColorStateList(context, R.color.quantum_grey300)
        } else {
            rippleColor = AppCompatResources.getColorStateList(context, R.color.quantum_grey700)
        }
    }
}

class GoogleLoginButton :
    MaterialButton {
    constructor(context: Context) : super(context) {
        configure()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        configure()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        configure()
    }

    private fun configure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            lineHeight = 26
        text = resources.getString(R.string.continue_with_google)
        isAllCaps = false
        icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_google, context.theme)
        iconTint = AppCompatResources.getColorStateList(context, R.color.transparent)
        iconTintMode = PorterDuff.Mode.SCREEN
        gravity = Gravity.CENTER
        iconGravity = ICON_GRAVITY_TEXT_START
        backgroundTintList = AppCompatResources.getColorStateList(context, R.color.colorWhite)
        style(R.style.Widget_MaterialComponents_Button_TextButton_Dialog_Flush)
        setTextColor(AppCompatResources.getColorStateList(context, R.color.colorBlack))
        if (!DarkModePrefManager(context).isNightMode) {
            rippleColor = AppCompatResources.getColorStateList(context, R.color.quantum_grey300)
        } else {
            rippleColor = AppCompatResources.getColorStateList(context, R.color.quantum_grey700)
        }
    }
}