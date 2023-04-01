package za.ac.iie.opsc.geoweather.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import za.ac.iie.opsc.geoweather.model.FiveDayForecast


interface IAccuWeather {

    /**
     * Get the five-day forecast for a specific location key
     * @param locationKey The key for the location
     * @param apiKey The api key to use
     * @param metric Whether to get the data in metric units of measurement
     * @return The five-day forecast
     */
    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String?,
        @Query("apikey") apiKey: String?,
        @Query("metric") metric: Boolean
    ): FiveDayForecast?

}