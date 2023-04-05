package za.ac.iie.opsc.geoweather.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import za.ac.iie.opsc.geoweather.model.FiveDayForecast
import za.ac.iie.opsc.geoweather.model.currentweather.CurrentWeather
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation


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

    /**
     * Get the current conditions at a location.
     * @param locationKey The key for the location
     * @param apiKey The api key to use
     * @return The current conditions at the location
     */
    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentConditions(
        @Path("locationKey") locationKey: String?,
        @Query("apikey") apiKey: String?
    ): List<CurrentWeather?>?

    /**
     * Gets the location data based on the geoposition.
     * @param geoposition The geoposition as latitude,longitude
     * @param apiKey The api key to use
     * @return The location data for the geoposition
     */
    @GET("locations/v1/cities/geoposition/search")
    suspend fun getLocationByPosition(
        @Query("q") geoposition: String?,
        @Query("apikey") apiKey: String?
    ): AccuWeatherLocation?

    /**
     * Get the location information for the top 150 cities worldwide.
     * @param apiKey The API key to use.
     * @return A list of 150 top cities.
     */
    @GET("locations/v1/topcities/150")
    suspend fun getTop150Cities(
        @Query("apikey") apiKey: String?
    ): List<AccuWeatherLocation?>?
}