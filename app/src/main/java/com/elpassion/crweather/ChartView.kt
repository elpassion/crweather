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
        val timeStart = chart.inputRange.start.toDateTimeString()
        val timeEnd = chart.inputRange.endInclusive.toDateTimeString()
        var textOffset = 16f
        canvas.drawStatus("Forecasts $timeStart ... $timeEnd   Updated ${chart.time.toTimeString()}", 4f, textOffset)
        textOffset += 16f
        for ((name, color, points) in chart.lines) {
            val valueStart = chart.outputRange.start.toMeasurementString()
            val valueEnd = chart.outputRange.endInclusive.toMeasurementString()
            canvas.drawStatus("$name  $valueStart ... $valueEnd", 4f, textOffset, color)
            textOffset += 16f
            paint.color = color
            buffer.clear()
            points.changes(buffer)
            for ((begin, end) in buffer)
                canvas.drawLine(begin.scale(chart.area, canvas.area) to end.scale(chart.area, canvas.area), paint)
            for (point in points)
                canvas.drawCircle(point.scale(chart.area, canvas.area), 2f, paint)
        }
    }
}