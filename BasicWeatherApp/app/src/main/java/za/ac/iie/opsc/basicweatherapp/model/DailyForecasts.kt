package za.ac.iie.opsc.basicweatherapp.model

import com.google.gson.annotations.SerializedName


data class DailyForecasts (

  @SerializedName("Date"        ) var Date        : String?           = null,
  @SerializedName("EpochDate"   ) var EpochDate   : Int?              = null,
  @SerializedName("Temperature" ) var Temperature : Temperature?      = Temperature(),
  @SerializedName("Day"         ) var Day         : Day?              = Day(),
  @SerializedName("Night"       ) var Night       : Night?            = Night(),
  @SerializedName("Sources"     ) var Sources     : ArrayList<String> = arrayListOf(),
  @SerializedName("MobileLink"  ) var MobileLink  : String?           = null,
  @SerializedName("Link"        ) var Link        : String?           = null

)