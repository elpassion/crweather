package com.elpassion.crweather

import android.graphics.Color.BLACK
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes

private val STATUS_PAINT = android.graphics.Paint().apply {
    textSize = 12f
    isAntiAlias = true
}

operator fun android.view.Menu.iterator() = object : Iterator<android.view.MenuItem> {
    private var current = 0
    override fun hasNext() = current < size()
    override fun next() = getItem(current++)
}

fun android.view.ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): android.view.View {
    val inflater = android.view.LayoutInflater.from(context)
    return inflater.inflate(layoutId, this, attachToRoot)
}

fun android.graphics.Canvas.drawCircle(point: Point, radius: Float, paint: android.graphics.Paint)
        = drawCircle(point.x, point.y, radius, paint)

fun android.graphics.Canvas.drawLine(points: Pair<Point, Point>, paint: android.graphics.Paint)
        = drawLine(points.first.x, points.first.y, points.second.x, points.second.y, paint)

fun android.graphics.Canvas.drawStatus(status: String, x: Float = 4f, y: Float = 16f, @ColorInt color: Int= BLACK)
        = drawText(status, x, y, com.elpassion.crweather.STATUS_PAINT.withColor(color))

private fun android.graphics.Paint.withColor(@ColorInt acolor: Int)
        = android.graphics.Paint().also { it.set(this); it.color = acolor }