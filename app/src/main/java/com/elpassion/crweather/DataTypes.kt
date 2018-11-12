package com.elpassion.crweather

import androidx.annotation.ColorInt


data class Chart(
        val inputRange: ClosedFloatingPointRange<Float>,
        val outputRange: ClosedFloatingPointRange<Float>,
        val lines: List<Line>,
        val timeMs: Long = currentTimeMs
)

data class Line(
        val name: String,
        @ColorInt val color: Int,
        val points: List<Point>
)

data class Point(var x: Float, var y: Float)

sealed class Action

data class SelectCity(val city: String) : Action()
