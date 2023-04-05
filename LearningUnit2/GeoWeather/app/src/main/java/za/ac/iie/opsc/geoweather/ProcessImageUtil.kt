package za.ac.iie.opsc.geoweather

import android.R.attr.mimeType
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream


object ProcessImageUtil {
    /**
     * Convert a view to a Bitmap.
     * @param view The view to convert.
     * @return The converted Bitmap.
     */
    fun takeScreenshot(view: View): Bitmap {
        val screenView: View = view.rootView
        val bitmap: Bitmap = screenView.drawToBitmap(Bitmap.Config.ARGB_8888)
        return bitmap
    }

    /**
     * Write a screenshot bitmap to a folder.
     * @param context The activity that this is called from.
     * @param bitmap The bitmap to write.
     * @param fileName The name of the file to create.
     */
    fun storeScreenshot(
        context: Context, bitmap: Bitmap,
        fileName: String
    ) {
        // Get the application's folder - no permissions required to write
        val directory: File? = context.getExternalFilesDir(null)
        if (directory != null && !directory.exists()) {
            val isCreated: Boolean = directory.mkdirs()
            Log.d("MakingDir", "Created: $isCreated")
        }
        val captureImage = File(directory, "$fileName.PNG")
        try {
            val writeImage = FileOutputStream(captureImage)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, writeImage)
            writeImage.flush()
            writeImage.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Share the saved image using an intent.
     * @param context The context where the call is made from.
     * @param filename The name of the file to share.
     */
    fun pushToInstagram(context: Context, filename: String) {
        val directory: File? = context.getExternalFilesDir(null)
        val type = "image/*"
        val mimeTypeArray = arrayOf<String>(type)
        val mediaPath = "$filename.PNG"
        val share = Intent(Intent.ACTION_SEND)
        share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.type = type
        val media = File(directory, mediaPath)
        var uri: Uri? = Uri.fromFile(media)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider", media
            )
        }
        share.clipData = ClipData.newRawUri("Sharing weather", uri)
        share.putExtra(Intent.EXTRA_STREAM, uri)
        share.putExtra(Intent.EXTRA_SUBJECT, "Sharing my weather")
        context.startActivity(Intent.createChooser(share, "share to"))
    }
}
