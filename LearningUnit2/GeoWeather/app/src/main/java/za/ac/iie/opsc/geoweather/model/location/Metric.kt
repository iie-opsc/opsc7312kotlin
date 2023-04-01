package za.ac.iie.opsc.geoweather.model.location

import com.google.gson.annotations.SerializedName


data class Metric (

  @SerializedName("Value"    ) var Value    : Double?    = null,
  @SerializedName("Unit"     ) var Unit     : String? = null,
  @SerializedName("UnitType" ) var UnitType : Int?    = null

)