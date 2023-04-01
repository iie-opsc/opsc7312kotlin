package za.ac.iie.opsc.geoweather.model.currentweather

import com.google.gson.annotations.SerializedName


data class Temperature (

  @SerializedName("Metric"   ) var Metric   : Metric?   = Metric(),
  @SerializedName("Imperial" ) var Imperial : Imperial? = Imperial()

)