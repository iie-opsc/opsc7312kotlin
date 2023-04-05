package za.ac.iie.opsc.geoweather

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import za.ac.iie.opsc.geoweather.model.currentweather.CurrentWeather
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [CityWeatherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityWeatherFragment : Fragment() {
    private var viewModel = CityWeatherModel()
    private lateinit var searchBar: MaterialSearchBar
    private lateinit var cityName: TextView
    private lateinit var weatherText: TextView
    private lateinit var temperature: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city_weather, container, false)
        viewModel.getCityList()
        searchBar = view.findViewById<MaterialSearchBar>(R.id.sb_city_name)
        cityName = view.findViewById<TextView>(R.id.tvCityName)
        weatherText = view.findViewById<TextView>(R.id.tvWeatherText)
        temperature = view.findViewById<TextView>(R.id.tvCurrentTemperature)

        setupViewModel()
        setupSearchBar()

        return view
    }

    private fun setupViewModel() {
        // observe the list in the model for changes
        val citiesObserver = Observer<List<AccuWeatherLocation?>> { newCities ->
            searchBar.lastSuggestions = viewModel.citiesHashMap.keys.toList().sorted()
        }
        viewModel.citiesList.observe(viewLifecycleOwner, citiesObserver)

        // observe the list in the model for changes
        val weatherObserver = Observer<CurrentWeather> { weather ->
            run {
                weatherText.text = weather.WeatherText
                temperature.text = "${weather.Temperature?.Metric?.Value} " +
                        "${weather.Temperature?.Metric?.Unit}"
            }
        }
        viewModel.currentWeather.observe(viewLifecycleOwner, weatherObserver)
    }

    private fun setupSearchBar() {
        searchBar.isEnabled = true
        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val suggest: MutableList<String> = ArrayList()
                for (city in viewModel.citiesHashMap.keys) {
                    if (city.lowercase(Locale.getDefault()).contains(
                            searchBar.getText().toLowerCase()
                        )
                    ) suggest.add(city)
                }
                Collections.sort(suggest)
                searchBar.setLastSuggestions(suggest)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        searchBar.setOnSearchActionListener(
            object : OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {}
                override fun onSearchConfirmed(text: CharSequence) {
                    Log.d("Search:", text.toString() + "")
                    cityName.text = text.toString()
                    viewModel.getCurrentWeather(
                        viewModel.citiesHashMap[text.toString()]?.Key!!)
                }
                override fun onButtonClicked(buttonCode: Int) {}
            })
    }
}