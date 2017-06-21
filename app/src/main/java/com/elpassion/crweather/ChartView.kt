package com.elpassion.crweather

import android.content.Context
import android.util.AttributeSet
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor


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
        for (chart in this) {
            draw { drawChart(chart) }
        }
    }
}