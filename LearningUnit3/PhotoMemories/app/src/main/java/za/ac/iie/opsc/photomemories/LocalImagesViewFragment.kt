package za.ac.iie.opsc.photomemories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import za.ac.iie.opsc.photomemories.database.DatabaseHandler
import za.ac.iie.opsc.photomemories.model.ImageModel


/**
 * A fragment representing a list of Items.
 */
class LocalImagesViewFragment : Fragment() {

    private var columnCount = 1
    private lateinit var imageDb: DatabaseHandler
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
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
                adapter = MyLocalImageModelRecyclerViewAdapter(list)
            }
            imageDb = DatabaseHandler(activity)
            getData()
        }
        return view
    }

    private fun getData() {
        try {
            val images = imageDb.readDisplayImages()
            if (images != null) {
                val photoViewAdapter =
                    MyLocalImageModelRecyclerViewAdapter(images)
                recyclerView.setHasFixedSize(true)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = photoViewAdapter
            } else {
                Toast.makeText(
                    context, "No Images found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            LocalImagesViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}