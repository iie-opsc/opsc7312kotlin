package za.ac.iie.opsc.geoweather.model.location

import com.google.gson.annotations.SerializedName


data class Elevation (

  @SerializedName("Metric"   ) var Metric   : Metric?   = Metric(),
  @SerializedName("Imperial" ) var Imperial : Imperial? = Imperial()

)