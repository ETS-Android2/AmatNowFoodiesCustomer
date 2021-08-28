package com.foodies.amatfoodies.utils


import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

import com.foodies.amatfoodies.R;

class DividerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private val mPaint: Paint
    private var orientation: Int = 0
    private val dashGap: Int
    private val dashLength: Int
    private val dashThickness: Int
    private var color: Int

    init {


        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.DividerView, 0, 0)

        try {
            dashGap = a.getDimensionPixelSize(R.styleable.DividerView_dashGap, 5)
            dashLength = a.getDimensionPixelSize(R.styleable.DividerView_dashLength, 5)
            dashThickness = a.getDimensionPixelSize(R.styleable.DividerView_dashThickness, 3)
            color = a.getColor(R.styleable.DividerView_lineColor, -0x1000000)
            orientation = a.getInt(R.styleable.DividerView_tb_orientation, ORIENTATION_HORIZONTAL)
        } finally {
            a.recycle()
        }

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = color
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = dashThickness.toFloat()
        mPaint.pathEffect =
            DashPathEffect(floatArrayOf(dashLength.toFloat(), dashGap.toFloat()), 0f)
    }

    fun setLineColor(mLineColor: Int) {
        color = mLineColor
        mPaint.color = mLineColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == ORIENTATION_HORIZONTAL) {
            val center = height * .5f
            canvas.drawLine(0f, center, width.toFloat(), center, mPaint)
        } else {
            val center = width * .5f
            canvas.drawLine(center, 0f, center, height.toFloat(), mPaint)
        }
    }

    companion object {
        var ORIENTATION_HORIZONTAL = 0
        var ORIENTATION_VERTICAL = 1
    }
}