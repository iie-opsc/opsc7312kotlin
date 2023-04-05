package za.ac.iie.opsc.photomemories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import za.ac.iie.opsc.photomemories.databinding.ActivityLocalImagesBinding

class CloudImagesActivity : AppCompatActivity() {

    private val storeFragment = CloudImagesStoreFragment()
    private val viewFragment = CloudImagesViewFragment()
    lateinit var binding: ActivityLocalImagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnAdd.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.local_image_place_holder, storeFragment)
            transaction.commitAllowingStateLoss()
        }
        binding.btnView.setOnClickListener {
            val manager: FragmentManager = supportFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            transaction.replace(R.id.local_image_place_holder, viewFragment)
            transaction.commitAllowingStateLoss()
        }
    }
}