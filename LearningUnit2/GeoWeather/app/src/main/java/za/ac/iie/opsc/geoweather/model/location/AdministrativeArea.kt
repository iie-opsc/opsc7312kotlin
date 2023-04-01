package za.ac.iie.opsc.geoweather.model.location

import com.google.gson.annotations.SerializedName


data class AdministrativeArea (

  @SerializedName("ID"            ) var ID            : String? = null,
  @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
  @SerializedName("EnglishName"   ) var EnglishName   : String? = null,
  @SerializedName("Level"         ) var Level         : Int?    = null,
  @SerializedName("LocalizedType" ) var LocalizedType : String? = null,
  @SerializedName("EnglishType"   ) var EnglishType   : String? = null,
  @SerializedName("CountryID"     ) var CountryID     : String? = null

)