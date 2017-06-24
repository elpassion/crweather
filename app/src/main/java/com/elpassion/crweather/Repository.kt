package com.elpassion.crweather


object Repository {

    internal val appid = "f08f5bce8d0fe0c0903f145a88d773f8"


    /**
     * @throws IllegalStateException
     */
    suspend fun getCityCharts(city: String): List<Chart> {
        val forecasts = getDailyForecastsByCity(city, 10, "metric").list?.toList() ?: return emptyList()
        forecasts.size > 1 || return emptyList()
        return listOf(
                forecasts.tempChart,
                forecasts.humidityAndCloudinessChart,
                forecasts.windSpeedChart,
                forecasts.pressureChart,
                forecasts.rainAndSnowChart
        )
        // TODO: more charts? use all private functions from below?
    }


    /**
     * @param city City name + optional country code after comma
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getForecastByCity(city: String, units: String? = null): OpenWeatherMapApi.Forecast
            = OpenWeatherMapApi.service.getForecastByCity(appid, city, units).await()

    /**
     * Up to five days forecasts with data every 3 hours for given city name
     * @param city City name + optional country code after comma
     * @param cnt Optional maximum number of forecasts in returned "list"
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getForecastsByCity(city: String, cnt: Long? = null, units: String? = null): OpenWeatherMapApi.Forecasts
            = OpenWeatherMapApi.service.getForecastsByCity(appid, city, cnt, units).await()

    /**
     * Up to 16 days daily forecasts for given city
     * @param city City name + optional country code after comma
     * @param cnt Optional maximum number of daily forecasts in returned "list"
     * @param units Default (null) means in Kelvin, "metric" means in Celsius, "imperial" means in Fahrenheit
     * @throws IllegalStateException
     */
    private suspend fun getDailyForecastsByCity(city: String, cnt: Long? = null, units: String? = null): OpenWeatherMapApi.DailyForecasts
            = OpenWeatherMapApi.service.getDailyForecastsByCity(appid, city, cnt, units).await()

}