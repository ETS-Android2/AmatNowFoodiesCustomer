package com.foodies.amatfoodies.utils

import android.content.Context
import android.graphics.Typeface
import java.util.*

object FontCache {
    private val fontCache = HashMap<String, Typeface?>()

    @JvmStatic
    fun getTypeface(fontName: String, context: Context): Typeface? {
        var typeface = fontCache[fontName]

        if (typeface == null) {
            typeface = try {
                Typeface.createFromAsset(context.assets, fontName)
            } catch (e: Exception) {
                return null
            }

            fontCache[fontName] = typeface
        }
        return typeface
    }
}