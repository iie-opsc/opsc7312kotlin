package za.ac.iie.opsc.geoweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import za.ac.iie.opsc.geoweather.model.currentweather.CurrentWeather

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "locationName"
private const val ARG_PARAM2 = "locationKey"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentWeatherFragment : Fragment() {

    private var viewModel = CurrentWeatherModel()
    private var locationName: String? = null
    private var locationKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locationName = it.getString(ARG_PARAM1)
            locationKey = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_weather,
                                    container, false)

        viewModel.getCurrentWeather(locationKey.toString())

        // observe the list in the model for changes
        val weatherObserver = Observer<CurrentWeather> { newWeather -> run {
                view.findViewById<TextView>(R.id.tvLocation).text = locationName
                view.findViewById<TextView>(R.id.tvDescription).text = newWeather.WeatherText
                view.findViewById<TextView>(R.id.tvTemperature).text =
                    "${newWeather.Temperature?.Metric?.Value} " +
                            "${newWeather.Temperature?.Metric?.Unit}"
            }
        }
        viewModel.currentWeather.observe(viewLifecycleOwner, weatherObserver)
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
            CurrentWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, locationName)
                    putString(ARG_PARAM2, locationKey)
                }
            }
    }
}
