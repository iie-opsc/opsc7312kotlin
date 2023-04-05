package za.ac.iie.opsc.geoweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient

class MainActivityModel : ViewModel() {

    private var _location = MutableLiveData<AccuWeatherLocation>()
    var location: LiveData<AccuWeatherLocation> = _location

    fun getLocation(geoposition: String) {
        viewModelScope.launch {
            try {
                val weatherData = RetrofitClient.weatherService?.
                    getLocationByPosition(geoposition,
                        BuildConfig.ACCUWEATHER_API_KEY)
                if (weatherData != null) {
                    _location.value = weatherData!!
                }
            } catch (ex: HttpException) {
                Log.e("Weather data", ex.message())
            }
        }
    }
}