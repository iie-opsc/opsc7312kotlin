package za.ac.iie.opsc.basicweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import za.ac.iie.opsc.basicweatherapp.model.ExampleJson2KtKotlin
import kotlin.concurrent.thread

/**
 * A fragment representing a list of Items.
 */
class DailyForecastsFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = 1
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                // Thanks to https://stackoverflow.com/questions/46177133/http-request-in-android-with-kotlin
                thread {
                    val weatherJSON = try {
                        buildURLForWeather()?.readText()
                    } catch (e: Exception) {
                        return@thread
                    }
                    if (weatherJSON != null) {
                        val gson = Gson()
                        val weatherData = gson.fromJson<ExampleJson2KtKotlin>(weatherJSON,
                            ExampleJson2KtKotlin::class.java)
                        activity?.runOnUiThread {
                            adapter = DailyForecastsRecyclerViewAdapter(weatherData.DailyForecasts)
                        }
                    }
                }
            }
        }
        return view
    }
}