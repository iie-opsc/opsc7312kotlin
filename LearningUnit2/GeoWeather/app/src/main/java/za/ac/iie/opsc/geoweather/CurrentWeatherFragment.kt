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
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CurrentWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentWeatherFragment : Fragment() {

    private var viewModel = CurrentWeatherModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_current_weather,
                                    container, false)

        viewModel.getCurrentWeather("305605")

        // observe the list in the model for changes
        val weatherObserver = Observer<CurrentWeather> { newWeather -> run {
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
         * this fragment.
         *
         * @return A new instance of fragment CurrentWeatherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            CurrentWeatherFragment().apply {
            }
    }
}
