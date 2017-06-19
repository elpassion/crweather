package com.elpassion.crweather

import android.graphics.Color
import com.elpassion.crweather.OpenWeatherMapApi.DailyForecast

private val CACHE_TIME = 1000L * 60L * 60L // one hour

private val BLUE_LIGHT = 0x220000FF
private val BLACK_LIGHT = 0x22000000


val Float.asMeasurementString get() = "%.2f".format(this)

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.tempChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = (minTemp ?: -30f) - 5f..(maxTemp ?: 70f) + 5f,
            lines = listOf(
                    Line("Maximum temperature (\u2103)", BLUE_LIGHT, toPoints { temp?.max }),
                    Line("Minimum temperature (\u2103)", BLACK_LIGHT, toPoints { temp?.min }),
                    Line("Day temperature (\u2103)", Color.BLUE, toPoints { temp?.day }),
                    Line("Night temperature (\u2103)", Color.BLACK, toPoints { temp?.night })
            )
    )
}

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.humidityAndCloudinessChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = -5f..105f,
            lines = listOf(
                    Line("Humidity (%)", Color.GREEN, toPoints { humidity.takeIf { it != 0 }?.toFloat() }),
                    Line("Cloudiness (%)", Color.BLUE, toPoints { clouds.takeIf { it != 0 }?.toFloat() })
            )
    )
}

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.windSpeedChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = (minWindSpeed ?: 0f) - 1f..(maxWindSpeed ?: 100f) + 1f,
            lines = listOf(
                    Line("Wind speed (meter/s)", Color.DKGRAY, toPoints { speed })
            )
    )
}

fun Map<String, List<Chart>>.getFreshCharts(city: String) = get(city)?.takeIf {
    it.isNotEmpty() && it.first().timeMs + CACHE_TIME > currentTimeMs
}


private val List<DailyForecast>.minTemp get() = map { it.temp?.min }.filterNotNull().min()

private val List<DailyForecast>.maxTemp get() = map { it.temp?.max }.filterNotNull().max()

private val List<DailyForecast>.minWindSpeed get() = map { it.speed }.filterNotNull().min()

private val List<DailyForecast>.maxWindSpeed get() = map { it.speed }.filterNotNull().max()


private fun DailyForecast.toPointOrNull(toValue: DailyForecast.() -> Float?)
        = toValue()?.let { Point(dt.toFloat(), it) }

private fun List<DailyForecast>.toPoints(toValue: DailyForecast.() -> Float?)
        = map { it.toPointOrNull(toValue) }.filterNotNull()
