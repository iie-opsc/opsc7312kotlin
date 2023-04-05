package za.ac.iie.opsc.photomemories

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import za.ac.iie.opsc.photomemories.databinding.FragmentLocalImagesStoreBinding
import za.ac.iie.opsc.photomemories.model.ImageModel


/**
 * A simple [Fragment] subclass.
 * Use the [CloudImagesStoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CloudImagesStoreFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    lateinit var binding: FragmentLocalImagesStoreBinding
    private var bitmap: Bitmap? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var myRef = Firebase.database.getReference("PhotoMemories")
    private var imageData: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupImageChooser()
        auth = Firebase.auth
        storage = Firebase.storage
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
            if (binding.txtImageDescription.text.toString().isNotEmpty() &&
                bitmap != null) {
                storeImage(binding.txtImageDescription.text.toString())
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
                imageData = uri
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

    private fun getFileExtensions(uri: Uri): String? {
        val contentResolver: ContentResolver =
            requireActivity().contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(
            contentResolver.getType(uri))
    }

    private fun storeImage(name: String) {
        try {
            if (imageData != null) {
                // upload the image to storage
                val fileRef: StorageReference = storage.reference.child(
                    System.currentTimeMillis().toString() + "." +
                            getFileExtensions(imageData!!)
                )
                var uploadTask = fileRef.putFile(imageData!!)
                var uriTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    fileRef.downloadUrl
                }.addOnSuccessListener { taskSnapshot ->
                    // write an entry to realtime database
                    var currentUser = auth.getCurrentUser()
                    val imageModel = ImageModel(
                        name.trim(),
                        null,
                        taskSnapshot.toString()
                    )
                    val uploadID = myRef.push().key
                    currentUser?.uid?.let {
                        myRef.child(it).child((uploadID)!!)
                            .setValue(imageModel)
                    }
                    Toast.makeText(
                        context,
                        "Photo loaded to the cloud :-)",
                        Toast.LENGTH_SHORT
                    ).show()

                    // reset the user interface
                    binding.imgImagepane.setImageResource(
                        R.drawable.photo)
                    binding.txtImageDescription.setText("")
                }
            } else {
                Toast.makeText(
                    context, "Please select an Image ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}