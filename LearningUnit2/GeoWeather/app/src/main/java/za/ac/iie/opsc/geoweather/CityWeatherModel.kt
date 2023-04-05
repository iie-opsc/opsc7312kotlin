package za.ac.iie.opsc.geoweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import za.ac.iie.opsc.geoweather.model.currentweather.CurrentWeather
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient

class CityWeatherModel : ViewModel() {

    // cities list
    private var _citiesList = MutableLiveData<List<AccuWeatherLocation?>>()
    var citiesList: LiveData<List<AccuWeatherLocation?>> = _citiesList
    var citiesHashMap = HashMap<String, AccuWeatherLocation>()

    // current weather
    private var _currentWeather = MutableLiveData<CurrentWeather>()
    var currentWeather: LiveData<CurrentWeather> = _currentWeather

    fun getCityList() {
        viewModelScope.launch {
            val citiesListFromApi = RetrofitClient.weatherService?.
                getTop150Cities(BuildConfig.ACCUWEATHER_API_KEY)
            if (citiesListFromApi != null) {
                populateHashmap(citiesListFromApi)
                _citiesList.value = citiesListFromApi!!
            }
        }
    }

    private fun populateHashmap(citiesListFromApi: List<AccuWeatherLocation?>?) {
        citiesHashMap.clear()
        if (citiesListFromApi != null) {
            for (city in citiesListFromApi) {
                citiesHashMap[city?.LocalizedName!!] = city
            }
        }
    }

    fun getCurrentWeather(locationKey: String) {
        viewModelScope.launch {
            val weatherData = RetrofitClient.weatherService?.
            getCurrentConditions(locationKey,
                BuildConfig.ACCUWEATHER_API_KEY)
            _currentWeather.value = weatherData?.get(0)
        }
    }
}