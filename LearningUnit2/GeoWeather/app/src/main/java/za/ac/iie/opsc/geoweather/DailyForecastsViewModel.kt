package za.ac.iie.opsc.geoweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import za.ac.iie.opsc.geoweather.model.DailyForecasts
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient

class DailyForecastsViewModel : ViewModel() {

    private var _fiveDayForecast = MutableLiveData<List<DailyForecasts>>()
    var fiveDayForecast: LiveData<List<DailyForecasts>> = _fiveDayForecast

    fun getFiveDayForecast(locationKey: String) {
        viewModelScope.launch {
            val weatherData = RetrofitClient.weatherService?.
                    getFiveDayForecast(locationKey,
                        BuildConfig.ACCUWEATHER_API_KEY, false)
            _fiveDayForecast.value = weatherData?.DailyForecasts
        }
    }
}