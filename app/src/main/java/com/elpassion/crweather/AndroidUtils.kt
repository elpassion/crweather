package com.elpassion.crweather

import android.graphics.Canvas
import android.graphics.Color.BLACK
import android.graphics.Paint
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.view.Menu
import android.view.ViewGroup

private val STATUS_PAINT = Paint().apply {
    textSize = 12f
    isAntiAlias = true
}

private val CHART_PAINT = Paint().apply {
    style = Paint.Style.STROKE
    strokeWidth = 3f
    isAntiAlias = true
}

operator fun Menu.iterator() = object : Iterator<android.view.MenuItem> {
    private var current = 0
    override fun hasNext() = current < size()
    override fun next() = getItem(current++)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): android.view.View {
    val inflater = android.view.LayoutInflater.from(context)
    return inflater.inflate(layoutId, this, attachToRoot)
}

fun Canvas.drawCircle(point: Point, radius: Float, paint: android.graphics.Paint)
        = drawCircle(point.x, point.y, radius, paint)

fun Canvas.drawLine(points: Pair<Point, Point>, paint: android.graphics.Paint)
        = drawLine(points.first.x, points.first.y, points.second.x, points.second.y, paint)

fun Canvas.drawStatus(status: String, x: Float = 4f, y: Float = 16f, @ColorInt color: Int= BLACK)
        = drawText(status, x, y, com.elpassion.crweather.STATUS_PAINT.withColor(color))

fun Canvas.drawChart(chart: Chart, paint: Paint = CHART_PAINT) {
    val timeStart = chart.inputRange.start.asTimeMs.asDateString
    val timeEnd = chart.inputRange.endInclusive.asTimeMs.asDateString
    var textOffset = 16f
    drawStatus("Forecasts $timeStart ... $timeEnd   Updated ${chart.timeMs.asTimeString}", 4f, textOffset)
    textOffset += 16f
    for ((name, color, points) in chart.lines) {
        val valueStart = chart.outputRange.start.asMeasurementString
        val valueEnd = chart.outputRange.endInclusive.asMeasurementString
        drawStatus("$name  $valueStart ... $valueEnd", 4f, textOffset, color)
        textOffset += 16f
        paint.color = color
        for ((begin, end) in points.changes())
            drawLine(begin.scale(chart.area, area) to end.scale(chart.area, area), paint)
        for (point in points)
            drawCircle(point.scale(chart.area, area), 2f, paint)
    }

}

private fun android.graphics.Paint.withColor(@ColorInt acolor: Int)
        = android.graphics.Paint().also { it.set(this); it.color = acolor }