package za.ac.iie.opsc.geoweather

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import za.ac.iie.opsc.geoweather.ProcessImageUtil.pushToInstagram
import za.ac.iie.opsc.geoweather.ProcessImageUtil.storeScreenshot
import za.ac.iie.opsc.geoweather.ProcessImageUtil.takeScreenshot
import za.ac.iie.opsc.geoweather.databinding.ActivityMainBinding
import za.ac.iie.opsc.geoweather.model.location.AccuWeatherLocation
import za.ac.iie.opsc.geoweather.ui.main.SectionsPagerAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var model = MainActivityModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        checkPermissionsAndRequestLocation();

        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            val rootview: View = window.decorView.rootView
            val currentScreenshot = takeScreenshot(rootview)
            storeScreenshot(
                this@MainActivity,
                currentScreenshot, "Weather Today"
            )
            pushToInstagram(
                this@MainActivity,
                "/Weather Today"
            )
        }
    }

    private fun checkPermissionsAndRequestLocation() {
        val hasFineLocationPermission = ActivityCompat.checkSelfPermission(
            this, "android.permission.ACCESS_FINE_LOCATION"
        )
        val hasCourseLocationPermission = ActivityCompat.checkSelfPermission(
            this, "android.permission.ACCESS_COARSE_LOCATION"
        )
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
            hasCourseLocationPermission != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf<String>(
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_COARSE_LOCATION"
            )
            // Request permission - this is asynchronous
            ActivityCompat.requestPermissions(this, permissions, 0)
        } else {
            // We have permission, so now ask for the location
            getLocationAndCreateUI()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
        // is this for our request?
        if (requestCode == 0) {
            if (grantResults.size > 0 &&
                (grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED)
            ) {
                getLocationAndCreateUI()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationAndCreateUI() {
        val locationRequest: LocationRequest = buildLocationRequest()
        val locationCallback: LocationCallback = buildLocationCallBack()
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,
            locationCallback, Looper.myLooper()
        )
    }

    private fun buildLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(3000)
        locationRequest.setSmallestDisplacement(10.0f)
        return locationRequest
    }

    private fun buildLocationCallBack(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location: Location = locationResult.lastLocation
                Log.i("LocationResult", "onLocationResult: $location")
                model.getLocation("${location.latitude}," +
                        "${location.longitude}")

                // observe the list in the model for changes
                val weatherObserver = Observer<AccuWeatherLocation> {
                    location -> displayData(location)
                }
                model.location.observe(this@MainActivity, weatherObserver)
            }
        }
    }

    fun displayData(location: AccuWeatherLocation) {
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this@MainActivity,
            supportFragmentManager,
            location.LocalizedName.toString(),
            location.Key.toString()
        )
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
    }
}