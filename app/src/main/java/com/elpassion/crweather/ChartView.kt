package com.elpassion.crweather

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet


class ChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : CrView(context, attrs, defStyleAttr, defStyleRes) {

    var chart: Chart = Chart(0f..100f, 0f..100f, emptyList())
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawChart(chart)
    }
}