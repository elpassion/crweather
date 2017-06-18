package com.elpassion.crweather

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.LayoutRes
import android.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine


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


suspend fun <T> Call<T>.await(): T = suspendCoroutine { continuation ->

    val callback = object : Callback<T> {

        override fun onFailure(call: Call<T>, t: Throwable) {
            continuation.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) = continuation.resumeNormallyOrWithException {
            response.isSuccessful || throw IllegalStateException("Http error ${response.code()}")
            response.body() ?: throw IllegalStateException("Response body is null")
        }
    }

    enqueue(callback) // TODO: cancellation (invoke Call.cancel() when coroutine is cancelled)
}

inline fun <T> Continuation<T>.resumeNormallyOrWithException(getter: () -> T) {
    try {
        val result = getter()
        resume(result)
    } catch (exception: Throwable) {
        resumeWithException(exception)
    }
}


val List<OpenWeatherMapApi.DailyForecast>.minTemp get() = map { it.temp?.min }.filterNotNull().min()

val List<OpenWeatherMapApi.DailyForecast>.maxTemp get() = map { it.temp?.max }.filterNotNull().max()

val List<OpenWeatherMapApi.DailyForecast>.minWindSpeed get() = map { it.speed }.filterNotNull().min()

val List<OpenWeatherMapApi.DailyForecast>.maxWindSpeed get() = map { it.speed }.filterNotNull().max()

val BLUE_LIGHT = 0x220000FF
val BLACK_LIGHT = 0x22000000

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<OpenWeatherMapApi.DailyForecast>.tempChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = (minTemp ?: -30f) - 5f..(maxTemp ?: 70f) + 5f,
            lines = listOf(
                    Line("Maximum temperature (\u2103C)", BLUE_LIGHT, toPoints { temp?.max }),
                    Line("Minimum temperature (\u2103C)", BLACK_LIGHT, toPoints { temp?.min }),
                    Line("Day temperature (\u2103C)", Color.BLUE, toPoints { temp?.day }),
                    Line("Night temperature (\u2103C)", Color.BLACK, toPoints { temp?.night })
            )
    )
}

/**
 * WARNING: The list has to have at least two forecasts
 */
val List<OpenWeatherMapApi.DailyForecast>.humidityAndCloudinessChart: Chart get() {

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
val List<OpenWeatherMapApi.DailyForecast>.windSpeedChart: Chart get() {

    require(size > 1) { "Can not create a chart with less then two measurements" } // maybe: return some chart for just one measurement?

    return Chart(
            inputRange = first().dt.toFloat()..last().dt.toFloat(),
            outputRange = (minWindSpeed ?: 0f) - 1f..(maxWindSpeed ?: 100f) + 1f,
            lines = listOf(
                    Line("Wind speed (meter/s)", Color.DKGRAY, toPoints { speed })
            )
    )
}

fun OpenWeatherMapApi.DailyForecast.toPointOrNull(toValue: OpenWeatherMapApi.DailyForecast.() -> Float?)
        = toValue()?.let { Point(dt.toFloat(), it) }

fun List<OpenWeatherMapApi.DailyForecast>.toPoints(toValue: OpenWeatherMapApi.DailyForecast.() -> Float?)
        = map { it.toPointOrNull(toValue) }.filterNotNull()
