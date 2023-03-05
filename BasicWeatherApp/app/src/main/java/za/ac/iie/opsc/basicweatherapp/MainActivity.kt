package za.ac.iie.opsc.basicweatherapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import za.ac.iie.opsc.basicweatherapp.databinding.ActivityMainBinding
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var fiveDaylList =  mutableListOf<Forecast>()
    val LOGGING_TAG = "weatherDATA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // add an event handler to open the Accu Weather website
        binding.ivAccuweather.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.accuweather.com/")
            )
            startActivity(intent)
        }

        // Thanks to https://stackoverflow.com/questions/46177133/http-request-in-android-with-kotlin
        thread {
            val weather = try {
                buildURLForWeather()?.readText()
            } catch (e: Exception) {
                return@thread
            }
            runOnUiThread { consumeJson(weather) }
        }
    }

    fun consumeJson(weatherJSON: String?) {
        if (fiveDaylList != null) {
            fiveDaylList.clear()
        }

        if (weatherJSON != null) {
            try {
                // get the root JSON object
                val rootWeatherData = JSONObject(weatherJSON)
                // find the daily forecasts array
                val fiveDayForecast = rootWeatherData.getJSONArray("DailyForecasts")

                // get data from each entry in the array
                for (i in 0 until fiveDayForecast.length()) {
                    val forecastObject = Forecast()
                    val dailyWeather = fiveDayForecast.getJSONObject(i)

                    // get date
                    val date = dailyWeather.getString("Date")
                    Log.i(LOGGING_TAG, "consumeJson: Date$date")
                    forecastObject.date = date

                    // get minimum temperature
                    val temperatureObject = dailyWeather.getJSONObject("Temperature")
                    val minTempObject = temperatureObject.getJSONObject("Minimum")
                    val minTemp = minTempObject.getString("Value")
                    Log.i(LOGGING_TAG, "consumeJson: minTemp$minTemp")
                    forecastObject.minimumTemperature = minTemp

                    // get maximum temperature
                    val maxTempObject = temperatureObject.getJSONObject("Maximum")
                    val maxTemp = maxTempObject.getString("Value")
                    Log.i(LOGGING_TAG, "consumeJson: maxTemp$maxTemp")
                    forecastObject.maximumTemperature = maxTemp
                    fiveDaylList.add(forecastObject)
                    binding.tvWeather.append(
                        "Date: $date Min: $minTemp Max: $maxTemp\n"
                    )
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
}