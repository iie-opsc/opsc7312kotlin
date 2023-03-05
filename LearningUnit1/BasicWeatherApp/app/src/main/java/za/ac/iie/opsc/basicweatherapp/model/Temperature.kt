package za.ac.iie.opsc.basicweatherapp.model

import com.google.gson.annotations.SerializedName
import za.ac.iie.opsc.basicweatherapp.model.Maximum
import za.ac.iie.opsc.basicweatherapp.model.Minimum


data class Temperature (

  @SerializedName("Minimum" ) var Minimum : Minimum? = Minimum(),
  @SerializedName("Maximum" ) var Maximum : Maximum? = Maximum()

)