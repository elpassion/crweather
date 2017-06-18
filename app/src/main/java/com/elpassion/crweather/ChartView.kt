package com.elpassion.crweather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class ChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    var chart: Chart = Chart(0f..100f, 0f..100f, emptyList())
        set(value) {
            field = value
            invalidate()
        }

    private val buffer = ArrayList<Pair<Point, Point>>(200) // to avoid allocations in onDraw

    override fun onDraw(canvas: Canvas) {
        // TODO: draw some legend, scale, lines names
        for ((_, color, points) in chart.lines) {
            paint.color = color
            buffer.clear()
            points.changes(buffer)
            for ((begin, end) in buffer)
                canvas.drawLine(begin.scale(chart.area, canvas.area) to end.scale(chart.area, canvas.area), paint)
            for (point in points)
                canvas.drawCircle(point.scale(chart.area, canvas.area), 3f, paint)
        }
    }
}