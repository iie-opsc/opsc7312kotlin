package za.ac.iie.opsc.basicweatherapp.model

import com.google.gson.annotations.SerializedName


data class Maximum (

  @SerializedName("Value"    ) var Value    : Double? = null,
  @SerializedName("Unit"     ) var Unit     : String? = null,
  @SerializedName("UnitType" ) var UnitType : Float?  = null

)