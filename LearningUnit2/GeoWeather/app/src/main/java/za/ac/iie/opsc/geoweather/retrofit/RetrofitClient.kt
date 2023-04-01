package za.ac.iie.opsc.geoweather.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var retrofit: Retrofit? = null
        get() {
            if (field == null) field = Retrofit.Builder()
                .baseUrl("https://dataservice.accuweather.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return field
        }

    var weatherService: IAccuWeather? = null
        get() {
            if (field == null) field = retrofit?.create(IAccuWeather::class.java)
            return field
        }
}
