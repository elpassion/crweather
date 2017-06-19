package com.elpassion.crweather

import android.graphics.Canvas
import android.graphics.RectF

fun Point.scale(from: RectF, to: RectF) = Point(
        x.scale(from.left..from.right, to.left..to.right),
        y.scale(from.top..from.bottom, to.top..to.bottom)
)

val Canvas.area get() = com.elpassion.crweather.area(widthRange, heightRange)

val Chart.area get() = com.elpassion.crweather.area(inputRange, outputRange.flip())

private val ClosedFloatingPointRange<Float>.span get() = endInclusive - start

private fun Float.scale(fromRange: ClosedFloatingPointRange<Float>, toRange: ClosedFloatingPointRange<Float>)
        = toRange.start + (this - fromRange.start) * toRange.span / fromRange.span

private fun area(xRange: ClosedFloatingPointRange<Float>, yRange: ClosedFloatingPointRange<Float>)
        = RectF(xRange.start, yRange.start, xRange.endInclusive, yRange.endInclusive)

private val Canvas.widthRange get() = 0f..width.toFloat()

private val Canvas.heightRange get() = 0f..height.toFloat()

private fun ClosedFloatingPointRange<Float>.flip() = endInclusive..start