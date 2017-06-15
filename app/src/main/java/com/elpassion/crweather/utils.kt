package com.elpassion.crweather

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.LayoutRes
import android.view.*


operator fun Menu.iterator() = object : Iterator<MenuItem> {
    private var current = 0
    override fun hasNext() = current < size()
    override fun next() = getItem(current++)
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int, attachToRoot: Boolean = false): View {
    val inflater = LayoutInflater.from(context)
    return inflater.inflate(layoutId, this, attachToRoot)
}

operator fun StringBuilder.plusAssign(string: String) {
    append(string)
}

fun <T> List<T>.changes(destination: MutableList<Pair<T, T>> = ArrayList(size)): MutableList<Pair<T, T>> {
    for (i in 0..size - 2)
        destination += get(i) to get(i + 1)
    return destination
}

val Canvas.widthRange get() = 0f..width.toFloat()

val Canvas.heightRange get() = 0f..height.toFloat()

fun Canvas.drawCircle(point: Point, radius: Float, paint: Paint) = drawCircle(point.x, point.y, radius, paint)

fun Canvas.drawLine(points: Pair<Point, Point>, paint: Paint) = drawLine(points.first.x, points.first.y, points.second.x, points.second.y, paint)

val ClosedFloatingPointRange<Float>.span get() = endInclusive - start

fun ClosedFloatingPointRange<Float>.flip() = endInclusive..start

fun Float.scale(fromRange: ClosedFloatingPointRange<Float>, toRange: ClosedFloatingPointRange<Float>) = toRange.start + (this - fromRange.start) * toRange.span / fromRange.span

fun Point.scale(from: RectF, to: RectF) = Point(
        x.scale(from.left..from.right, to.left..to.right),
        y.scale(from.top..from.bottom, to.top..to.bottom)
)

fun area(xRange: ClosedFloatingPointRange<Float>, yRange: ClosedFloatingPointRange<Float>) = RectF(xRange.start, yRange.start, xRange.endInclusive, yRange.endInclusive)

val Canvas.area get() = area(widthRange, heightRange)

val Chart.area get() = area(inputRange, outputRange.flip())
