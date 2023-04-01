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
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "locationName"
private const val ARG_PARAM2 = "locationKey"
class DailyForecastsFragment : Fragment() {

    private var columnCount = 1
    private var viewModel = DailyForecastsViewModel()
    private var locationName: String? = null
    private var locationKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            locationName = it.getString(ARG_PARAM1)
            locationKey = it.getString(ARG_PARAM2)
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
                viewModel.getFiveDayForecast(locationKey.toString())

                // observe the list in the model for changes
                val weatherObserver = Observer<List<DailyForecasts>> { newWeather ->
                    adapter = DailyForecastsRecyclerViewAdapter(newWeather)
                }
                viewModel.fiveDayForecast.observe(viewLifecycleOwner, weatherObserver)
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(locationName: String, locationKey: String) =
            DailyForecastsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, locationName)
                    putString(ARG_PARAM2, locationKey)
                }
            }
    }
}