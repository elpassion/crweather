package com.elpassion.crweather

import com.squareup.moshi.Moshi
import org.junit.Ignore
import org.junit.Test

@Ignore
class OpenWeatherMapApiIntegrationTests {

    val appid = Repository.appid

    // Moshi adapters just for logging..
    val moshi = Moshi.Builder().build()!!
    val moshiForecastAdapter = moshi.adapter(OpenWeatherMapApi.Forecast::class.java)!!
    val moshiForecastsAdapter = moshi.adapter(OpenWeatherMapApi.Forecasts::class.java)!!
    val moshiDailyForecastsAdapter = moshi.adapter(OpenWeatherMapApi.DailyForecasts::class.java)!!

    fun log(msg: Any?) = println(msg)
    fun log(forecast: OpenWeatherMapApi.Forecast?) = log(moshiForecastAdapter.toJson(forecast))
    fun log(forecasts: OpenWeatherMapApi.Forecasts?) = log(moshiForecastsAdapter.toJson(forecasts))
    fun log(dailyForecasts: OpenWeatherMapApi.DailyForecasts?) = log(moshiDailyForecastsAdapter.toJson(dailyForecasts))

    val cities = listOf("Wroclaw", "Warsaw", "London", "New York")

    @Test fun logForecastForGivenCities() {
        for (city in cities)
            log(getForecastByCity(city))
    }

    @Test fun logForecastsForGivenCities() {
        for (city in cities)
            log(getForecastsByCity(city))
    }

    @Test fun logDailyForecastsForGivenCities() {
        for (city in cities)
            log(getDailyForecastsByCity(city))
    }

    fun getForecastByCity(city: String): OpenWeatherMapApi.Forecast? {
        val call = OpenWeatherMapApi.service.getForecastByCity(appid, city, "metric")
        val response = call.execute()
        val body = response.body()
        return body
    }

    fun getForecastsByCity(city: String): OpenWeatherMapApi.Forecasts? {
        val call = OpenWeatherMapApi.service.getForecastsByCity(appid, city, 32, "metric")
        val response = call.execute()
        val body = response.body()
        return body
    }

    fun getDailyForecastsByCity(city: String): OpenWeatherMapApi.DailyForecasts? {
        val call = OpenWeatherMapApi.service.getDailyForecastsByCity(appid, city, 16, "metric")
        val response = call.execute()
        val body = response.body()
        return body
    }
}