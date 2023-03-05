package za.ac.iie.opsc.basicweatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import za.ac.iie.opsc.basicweatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}