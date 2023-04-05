package za.ac.iie.opsc.geoweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import za.ac.iie.opsc.geoweather.model.DailyForecasts
import za.ac.iie.opsc.geoweather.retrofit.RetrofitClient

class DailyForecastsViewModel : ViewModel() {

    private var _fiveDayForecast = MutableLiveData<List<DailyForecasts>>()
    var fiveDayForecast: LiveData<List<DailyForecasts>> = _fiveDayForecast

    fun getFiveDayForecast(locationKey: String) {
        viewModelScope.launch {
            try {
                val weatherData = RetrofitClient.weatherService?.
                getFiveDayForecast(locationKey,
                    BuildConfig.ACCUWEATHER_API_KEY, false)
                _fiveDayForecast.value = weatherData?.DailyForecasts
            } catch (ex: HttpException) {
                Log.e("Weather data", ex.message())
            }
        }
    }
}