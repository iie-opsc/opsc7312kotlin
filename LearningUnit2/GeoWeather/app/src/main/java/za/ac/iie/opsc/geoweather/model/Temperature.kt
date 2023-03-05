package za.ac.iie.opsc.geoweather.model

import com.google.gson.annotations.SerializedName
import za.ac.iie.opsc.geoweather.model.Maximum
import za.ac.iie.opsc.geoweather.model.Minimum


data class Temperature (

  @SerializedName("Minimum" ) var Minimum : Minimum? = Minimum(),
  @SerializedName("Maximum" ) var Maximum : Maximum? = Maximum()

)