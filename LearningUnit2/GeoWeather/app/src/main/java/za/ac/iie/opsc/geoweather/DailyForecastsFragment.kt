package za.ac.iie.opsc.geoweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.ac.iie.opsc.geoweather.model.DailyForecasts
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient
import kotlin.concurrent.thread

/**
 * A fragment representing a list of Items.
 */
class DailyForecastsFragment : Fragment() {

    private var columnCount = 1
    private var viewModel = DailyForecastsViewModel()

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

                // call the webservice
                viewModel.getFiveDayForecast("305605")

                // observe the list in the model for changes
                val weatherObserver = Observer<List<DailyForecasts>> { newWeather ->
                    adapter = DailyForecastsRecyclerViewAdapter(newWeather)
                }
                viewModel.fiveDayForecast.observe(viewLifecycleOwner, weatherObserver)
            }
        }
        return view
    }
}