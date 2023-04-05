package za.ac.iie.opsc.geoweather.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import za.ac.iie.opsc.geoweather.CityWeatherFragment
import za.ac.iie.opsc.geoweather.CurrentWeatherFragment
import za.ac.iie.opsc.geoweather.DailyForecastsFragment
import za.ac.iie.opsc.geoweather.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context,
                           fm: FragmentManager,
                           private val locationName: String,
                           private val locationKey: String) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        when (position) {
            0 -> return CurrentWeatherFragment.newInstance(locationName, locationKey)
            1 -> return DailyForecastsFragment.newInstance(locationName, locationKey)
            2 -> return CityWeatherFragment()
        }
        // Return a PlaceholderFragment.
        return PlaceholderFragment.newInstance(position + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}