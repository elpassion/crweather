package com.elpassion.crweather

import android.graphics.Color
import com.elpassion.crweather.OpenWeatherMapApi.DailyForecast
import java.lang.Math.signum

private val CACHE_TIME = 1000L * 60L * 60L // one hour

private val BLUE_LIGHT = 0x220000FF
private val BLACK_LIGHT = 0x22000000


val Float.asMeasurementString get() = "%.0f".format(this)

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.tempChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = 0f..40f,
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
            outputRange = 0f..100f,
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
            outputRange = 0f..15f,
            lines = listOf(
                    Line("Wind speed (meter/s)", Color.DKGRAY, toPoints { speed })
            )
    )
}

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.pressureChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = 950f..1050f,
            lines = listOf(
                    Line("Pressure (hPa)", Color.RED, toPoints { pressure })
            )
    )
}

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<DailyForecast>.rainAndSnowChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = 0f..100f,
            lines = listOf(
                    Line("Rain (%)", Color.BLUE, toPoints { rain }),
                    Line("Snow (%)", Color.CYAN, toPoints { snow })
            )
    )
}

fun Map<String, List<Chart>>.getFreshCharts(city: String) = get(city)?.takeIf {
    it.isNotEmpty() && it.first().timeMs + CACHE_TIME > currentTimeMs
}

private fun List<DailyForecast>.toPoints(toValue: DailyForecast.() -> Float?)
        = mapNotNull { it.toPointOrNull(toValue) }

private fun DailyForecast.toPointOrNull(toValue: DailyForecast.() -> Float?)
        = toValue()?.let { Point(dt.toFloat(), it) }


fun Chart.deepCopy() = copy(lines = lines.map { it.deepCopy() })

fun Line.deepCopy() = copy(points = points.map { it.copy() })

fun Chart.resetPoints() = apply { lines.forEach { it.points.forEach { point -> point.x = 0f; point.y = 0f } } }

fun Chart.moveABitTo(destination: Chart, velocities: Chart) {
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
        newVelocity /= 1.5f
    return newVelocity
}

fun Chart.copyAndReformat(model: Chart, defaultNewPoint: Point) = Chart(model.inputRange, model.outputRange, lines.copyAndReformatLines(model.lines, defaultNewPoint), model.timeMs)

private fun List<Line>.copyAndReformatLines(model: List<Line>, defaultPoint: Point) = List(model.size) { idx ->
    getOrElse(idx) { model[idx] }.copyAndReformat(model[idx], defaultPoint)
}

private fun Line.copyAndReformat(model: Line, defaultPoint: Point) = Line(model.name, model.color, points.copyAndReformatPoints(model.points, defaultPoint))

private fun List<Point>.copyAndReformatPoints(model: List<Point>, defaultPoint: Point) = List(model.size) { idx ->
    getOrElse(idx) { defaultPoint }.copy()
}

val Chart.pointAtTheEnd get() = Point(inputRange.endInclusive, outputRange.portion(.5f))

