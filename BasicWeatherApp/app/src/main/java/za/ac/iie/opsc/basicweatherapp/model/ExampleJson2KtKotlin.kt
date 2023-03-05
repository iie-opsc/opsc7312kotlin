package za.ac.iie.opsc.basicweatherapp.model

import com.google.gson.annotations.SerializedName


data class ExampleJson2KtKotlin (

  @SerializedName("Headline"       ) var Headline       : Headline?                 = Headline(),
  @SerializedName("DailyForecasts" ) var DailyForecasts : ArrayList<DailyForecasts> = arrayListOf()

)