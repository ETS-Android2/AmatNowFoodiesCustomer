package com.foodies.amatfoodies.utils

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.*

object FontUtils {
    var sTypeface: Typeface? = null

    /**
     * Load font from filePath
     *
     * @param context  context
     * @param fileName font file name
     * @return typeface
     */
    @JvmStatic
    fun loadFont(context: Context, fileName: String?): Typeface? {
        sTypeface = Typeface.createFromAsset(context.assets, fileName)
        return sTypeface
    }

    /**
     * Sets the font on all TextViews in the ViewGroup. Searches recursively for
     * all inner ViewGroups as well. Just add a check for any other views you
     * want to set as well (EditText, etc.)
     */
    @JvmStatic
    fun setFont(group: ViewGroup) {
        val count = group.childCount
        var v: View?
        for (i in 0 until count) {
            v = group.getChildAt(i)
            if (v is TextView) {
                v.typeface = sTypeface
            } else if (v is ViewGroup) setFont(v)
        }
    }

    /**
     * Sets the font on TextView
     */
    @JvmStatic
    fun setFont(v: View?) {
        if (v is TextView) {
            v.typeface = sTypeface
        }
    }

    /**
     * Load font from res/raw
     *
     *
     * Font in Android Library - Stack Overflow
     * http://stackoverflow.com/questions/7610355/font-in-android-library
     *
     * @param context    Context
     * @param resourceId resourceId
     * @return Typeface or null
     */
    @JvmStatic
    fun getTypefaceFromRaw(context: Context, resourceId: Int): Typeface? {
        var inputStream: InputStream? = null
        var bos: BufferedOutputStream? = null
        var os: OutputStream? = null
        var typeface: Typeface? = null
        try {
            // Load font(in res/raw) to memory
            inputStream = context.resources.openRawResource(resourceId)

            // Output font to temporary file
            val fontFilePath =
                context.cacheDir.toString() + "/tmp" + System.currentTimeMillis() + ".raw"
            os = FileOutputStream(fontFilePath)
            bos = BufferedOutputStream(os)
            val buffer = ByteArray(inputStream.available())
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                bos.write(buffer, 0, length)
            }

            // When loading completed, delete temporary files
            typeface = Typeface.createFromFile(fontFilePath)
            File(fontFilePath).delete()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            tryClose(bos)
            tryClose(os)
            tryClose(inputStream)
        }
        return typeface
    }

    /**
     * Release closeable object
     *
     * @param obj closeable object
     */
    private fun tryClose(obj: Closeable?) {
        if (obj != null) {
            try {
                obj.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}