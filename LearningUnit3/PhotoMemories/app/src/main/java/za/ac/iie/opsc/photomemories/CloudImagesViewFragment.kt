package za.ac.iie.opsc.photomemories

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import za.ac.iie.opsc.photomemories.model.ImageModel


/**
 * A fragment representing a list of Items.
 */
class CloudImagesViewFragment : Fragment() {

    private var columnCount = 1
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var myRef = Firebase.database.getReference("PhotoMemories")
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_local_images_view_list,
            container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                var list = mutableListOf<ImageModel>()
                adapter = MyCloudImageModelRecyclerViewAdapter(list)
                readData()
            }
        }
        return view
    }

    private fun readData() {
        var cloudPicsList = arrayListOf<ImageModel>()
        val user: String? = auth.currentUser?.uid
        val userReference: DatabaseReference = myRef.child(user!!)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cloudImages in dataSnapshot.children) {
                    val imageModel = cloudImages.getValue(
                        ImageModel::class.java)
                    if (imageModel != null) {
                        cloudPicsList.add(imageModel)
                    }
                }
                var cloudPhotoViewAdapter =
                    MyCloudImageModelRecyclerViewAdapter(cloudPicsList)
                recyclerView.setAdapter(cloudPhotoViewAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    context, databaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CloudImagesViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}