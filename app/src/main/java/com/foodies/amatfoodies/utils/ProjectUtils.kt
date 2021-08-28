package com.foodies.amatfoodies.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.format.Time
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import java.net.URL
import java.util.*

object ProjectUtils {
    const val TAG = "ProjectUtils"
    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    @JvmStatic
    fun changeStatusBarColor(activity: Activity, color: Int) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ResourcesCompat.getColor(activity.resources, color, activity.theme)
    }

    @JvmStatic
    @Suppress("DEPRECATION")
    fun fullscreen(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = activity.window.insetsController

            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    @JvmStatic
    fun statusBarBackgroundFromDrawable(activity: Activity, drawable: Int) {
        val window = activity.window
        val background =
            ResourcesCompat.getDrawable(activity.resources, drawable, activity.theme)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = activity.resources.getColor(android.R.color.transparent)
        // window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
        window.setBackgroundDrawable(background)
    }

    @JvmStatic
    fun statusBarBackgroundTransformURL(activity: Activity, image: String?) {
        val url: URL?
        var bmp: Bitmap? = null

        try {
            url = URL(image)
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val window = activity.window
        val background: Drawable = BitmapDrawable(activity.resources, bmp)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ResourcesCompat.getColor(
            activity.resources,
            android.R.color.transparent,
            activity.theme
        )
        // window.setNavigationBarColor(activity.getResources().getColor(R.color.transparent));
        window.setBackgroundDrawable(background)
    }

    @JvmStatic
    fun containsOnlyNumbers(str: String): Boolean {
        for (element in str) {
            if (!Character.isDigit(element)) return false
        }
        return true
    }

    /**
     * Displays a short Toast
     */
    @JvmStatic
    fun showToast(context: Context?, message: String?) {
        var toast: Toast? = null
        if (message == null) return
        if (toast == null && context != null) toast =
            Toast.makeText(context, message, Toast.LENGTH_LONG)
        if (toast != null) toast.setText(message)
        toast?.show()
    }

    @JvmOverloads
    @JvmStatic
    fun log(msg: String?, tr: Throwable? = null) = log(TAG, msg, tr)

    @JvmStatic
    fun log(tag: String?, msg: String?) = log(tag, msg, null)

    @JvmStatic
    fun log(tag: String?, msg: String?, tr: Throwable? = null) = Log.wtf(tag, msg, tr)

    /**
     * Returns the current time in milliseconds
     */
    @JvmStatic
    fun currentTimeInMillis(): Long {
        val time = Time()
        time.setToNow()
        return time.toMillis(false)
    }
}

private fun CharSequence?.capitalizeAllFirstInWord(): String {
    var value = ""
    var s2: String
    val tokenizer = StringTokenizer(this as String?)
    while (tokenizer.hasMoreTokens()) {
        s2 = tokenizer.nextToken().lowercase(Locale.getDefault())
        value += if (value.isEmpty()) s2.substring(0, 1)
            .uppercase(Locale.getDefault()) + s2.substring(1) else " " + s2.substring(0, 1)
            .uppercase(Locale.getDefault()) + s2.substring(
            1
        )
    }
    return value
}

private fun CharSequence?.capitalizeFirst(): String {
    var `val` = ""
    val _this = this as String
    try {
        `val` = Character.toUpperCase(_this[0]).toString() + _this.substring(1)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return `val`
}
