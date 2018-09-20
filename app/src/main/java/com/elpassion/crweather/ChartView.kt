package com.elpassion.crweather

import android.content.Context
import android.util.AttributeSet
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.isActive


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

    private val actor = GlobalScope.actor<Chart>(Dispatchers.Main, Channel.CONFLATED) {

        var currentChart = chart.deepCopy()
        var currentVelocities = chart.deepCopy().resetPoints()

        doOnDraw { drawChart(currentChart) }

        for (destinationChart in this) {

            currentChart = currentChart.copyAndReformat(destinationChart, destinationChart.pointAtTheEnd)
            currentVelocities = currentVelocities.copyAndReformat(destinationChart, Point(0f, 0f))

            while (isActive && isEmpty) {
                currentChart.moveABitTo(destinationChart, currentVelocities)
                redraw()
                delay(16)
            }
        }
    }
}
