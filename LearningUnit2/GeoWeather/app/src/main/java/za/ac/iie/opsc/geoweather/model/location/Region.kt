package za.ac.iie.opsc.geoweather.model.location

import com.google.gson.annotations.SerializedName


data class Region (

  @SerializedName("ID"            ) var ID            : String? = null,
  @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
  @SerializedName("EnglishName"   ) var EnglishName   : String? = null

)