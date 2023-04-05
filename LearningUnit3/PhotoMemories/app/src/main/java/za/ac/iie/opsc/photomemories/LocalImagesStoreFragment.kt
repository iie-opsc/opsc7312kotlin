package za.ac.iie.opsc.photomemories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import za.ac.iie.opsc.photomemories.database.DatabaseHandler
import za.ac.iie.opsc.photomemories.databinding.FragmentLocalImagesStoreBinding
import za.ac.iie.opsc.photomemories.model.ImageModel


/**
 * A simple [Fragment] subclass.
 * Use the [LocalImagesStoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocalImagesStoreFragment : Fragment() {
    lateinit var binding: FragmentLocalImagesStoreBinding
    private var bitmap: Bitmap? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var imagedb: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupImageChooser()
        imagedb = DatabaseHandler(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLocalImagesStoreBinding.inflate(inflater)
        binding.btnChooseImage.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
        binding.btnSave.setOnClickListener {
            if (binding.txtImageDescription.text.toString() != null &&
                bitmap != null) {
                val imageToStore = ImageModel(
                    binding.txtImageDescription.text.toString(), bitmap, null
                )
                imagedb.storeImageLocal(imageToStore)
            }
        }
        return binding.root
    }

    private fun setupImageChooser() {
        // From https://developer.android.com/training/data-storage/shared/photopicker
        pickMedia = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                val inputStream = context?.contentResolver?.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imgImagepane.setImageBitmap(bitmap)
            } else {
                Toast.makeText(
                    context, "No image selected",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}