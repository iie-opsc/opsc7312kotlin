package za.ac.iie.opsc.geoweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient

class MainActivityModel : ViewModel() {

    private var _location = MutableLiveData<AccuWeatherLocation>()
    var location: LiveData<AccuWeatherLocation> = _location

    fun getLocation(geoposition: String) {
        viewModelScope.launch {
            val weatherData = RetrofitClient.weatherService?.
                getLocationByPosition(geoposition,
                    BuildConfig.ACCUWEATHER_API_KEY)
            if (weatherData != null) {
                _location.value = weatherData!!
            }
        }
    }
}