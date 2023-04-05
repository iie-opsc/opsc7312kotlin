package za.ac.iie.opsc.geoweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
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
            try {
                val citiesListFromApi =
                    RetrofitClient.weatherService?.getTop150Cities(BuildConfig.ACCUWEATHER_API_KEY)
                if (citiesListFromApi != null) {
                    populateHashmap(citiesListFromApi)
                    _citiesList.value = citiesListFromApi!!
                }
            } catch (ex: HttpException) {
                Log.e("Weather data", ex.message())
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
            try {
                val weatherData = RetrofitClient.weatherService?.getCurrentConditions(
                    locationKey,
                    BuildConfig.ACCUWEATHER_API_KEY
                )
                _currentWeather.value = weatherData?.get(0)
            } catch (ex: HttpException) {
                Log.e("Weather data", ex.message())
            }
        }
    }
}