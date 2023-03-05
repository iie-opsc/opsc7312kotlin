package za.ac.iie.opsc.basicweatherapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import za.ac.iie.opsc.basicweatherapp.databinding.ActivityMainBinding
import za.ac.iie.opsc.basicweatherapp.model.ExampleJson2KtKotlin
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

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
        if (weatherJSON != null) {
            val gson = Gson()
            val weatherData = gson.fromJson<ExampleJson2KtKotlin>(weatherJSON,
                ExampleJson2KtKotlin::class.java)
            for(forecast in weatherData.DailyForecasts) {
                binding.tvWeather.append("Date: " +
                        forecast.Date?.substring(0, 10) +
                        " Min: " +
                        forecast.Temperature?.Minimum?.Value +
                        " Max: " +
                        forecast.Temperature?.Maximum?.Value +
                        "\n")
            }
        }
    }
}