package za.ac.iie.opsc.geoweather.model.location

import com.google.gson.annotations.SerializedName


data class TimeZone (

  @SerializedName("Code"             ) var Code             : String?  = null,
  @SerializedName("Name"             ) var Name             : String?  = null,
  @SerializedName("GmtOffset"        ) var GmtOffset        : Float?   = null,
  @SerializedName("IsDaylightSaving" ) var IsDaylightSaving : Boolean? = null,
  @SerializedName("NextOffsetChange" ) var NextOffsetChange : String?  = null

)