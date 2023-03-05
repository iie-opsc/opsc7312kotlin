package za.ac.iie.opsc.basicweatherapp.model

import com.google.gson.annotations.SerializedName


data class Headline (

  @SerializedName("EffectiveDate"      ) var EffectiveDate      : String? = null,
  @SerializedName("EffectiveEpochDate" ) var EffectiveEpochDate : Int?    = null,
  @SerializedName("Severity"           ) var Severity           : Int?    = null,
  @SerializedName("Text"               ) var Text               : String? = null,
  @SerializedName("Category"           ) var Category           : String? = null,
  @SerializedName("EndDate"            ) var EndDate            : String? = null,
  @SerializedName("EndEpochDate"       ) var EndEpochDate       : Int?    = null,
  @SerializedName("MobileLink"         ) var MobileLink         : String? = null,
  @SerializedName("Link"               ) var Link               : String? = null

)