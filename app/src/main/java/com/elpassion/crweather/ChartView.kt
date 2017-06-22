package com.elpassion.crweather

import android.content.Context
import android.util.AttributeSet
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import java.lang.Math.signum


class ChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : CrView(context, attrs, defStyleAttr, defStyleRes) {

    var chart: Chart = Chart(0f..100f, 0f..100f, emptyList())
        set(value) {
            field = value
            actor.offer(value)
        }

    val actor = actor<Chart>(UI, Channel.CONFLATED) {
        var currentChart = chart.deepCopy()
        var currentVelocities = chart.deepCopy().stop()
        for (newChart in this) {
            currentChart = currentChart.copyAndReformat(newChart)
            currentVelocities = currentVelocities.copyAndReformat(newChart)
            while (isActive && isEmpty) {
                draw { drawChart(currentChart) }
                delay(10)
                currentChart.mutateOneStepTowards(destination = newChart, velocities = currentVelocities)
            }
        }
    }

    companion object {

        private fun Chart.stop() = apply { lines.forEach { it.points.forEach { it.x = 0f; it.y = 0f } } }

        private fun Chart.mutateOneStepTowards(destination: Chart, velocities: Chart) {
            for ((lineIdx, line) in lines.withIndex())
                for ((pointIdx, point) in line.points.withIndex()) {
                    val velocityPoint = velocities.lines[lineIdx].points[pointIdx]
                    val destinationPoint = destination.lines[lineIdx].points[pointIdx]
                    point.x += velocityPoint.x
                    point.y += velocityPoint.y
                    velocityPoint.x = updateVelocity(velocityPoint.x, point.x, destinationPoint.x)
                    velocityPoint.y = updateVelocity(velocityPoint.y, point.y, destinationPoint.y)
                }
        }

        private fun updateVelocity(velocity: Float, currentPosition: Float, destinationPosition: Float): Float {
            var newVelocity = velocity + (destinationPosition - currentPosition) / 20f
            if (signum(newVelocity) != signum(destinationPosition - currentPosition))
                newVelocity = newVelocity / 1.5f
            return newVelocity
        }

        private fun Chart.copyAndReformat(model: Chart) = Chart(model.inputRange, model.outputRange, lines.copyAndReformatLines(model.lines), model.timeMs)

        private fun List<Line>.copyAndReformatLines(model: List<Line>) = MutableList(model.size) { idx ->
            getOrElse(idx) { model[idx] }.copyAndReformat(model[idx])
        }

        private fun Line.copyAndReformat(model: Line) = Line(model.name, model.color, points.copyAndReformatPoints(model.points))

        private fun List<Point>.copyAndReformatPoints(model: List<Point>) = MutableList(model.size) { idx ->
            getOrElse(idx) { model[idx] }.copy()
        }

        private fun Chart.deepCopy() = copy(lines = lines.map { it.deepCopy() })
        private fun Line.deepCopy() = copy(points = points.map { it.copy() })
    }
}