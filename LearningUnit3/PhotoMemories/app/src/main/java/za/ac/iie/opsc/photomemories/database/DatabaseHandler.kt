package za.ac.iie.opsc.photomemories.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import za.ac.iie.opsc.photomemories.model.ImageModel
import java.io.ByteArrayOutputStream


private const val DATABASE_NAME = "images.db"
private const val DATABASE_VERSION = 1
private const val createTableQuery =
    "create table imageStore(imageName TEXT, imageBitmap BLOB)"

class DatabaseHandler(private val context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null,
        DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(createTableQuery)
            Toast.makeText(
                context, "Table created",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context, e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int,
                           newVersion: Int) {
    }

    fun storeImageLocal(imageModel: ImageModel) {
        try {
            val imageDatabase = this.writableDatabase
            if (imageModel.imageBitmap != null) {
                val imageToStore: Bitmap = imageModel.imageBitmap
                val convertBitmapToByteArray = ByteArrayOutputStream()
                imageToStore.compress(
                    Bitmap.CompressFormat.JPEG,
                    100, convertBitmapToByteArray
                )
                val imageInBytes = convertBitmapToByteArray.toByteArray()
                val contentValues = ContentValues()
                contentValues.put("imageName", imageModel.imageName)
                contentValues.put("imageBitmap", imageInBytes)
                val checkIfQueryRuns = imageDatabase.insert(
                    "imageStore",
                    null, contentValues
                )
                if (checkIfQueryRuns != -1L) {
                    Toast.makeText(
                        context, "Image saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    imageDatabase.close()
                } else {
                    Toast.makeText(
                        context, "Unable to save Image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: java.lang.Exception) {
            Log.i("SAVE TO DB ", "storeImageLocal: " + e.message)
        }
    }

    fun readDisplayImages(): ArrayList<ImageModel>? {
        return try {
            val imagDatabase = this.readableDatabase
            val dbImages: ArrayList<ImageModel> = ArrayList()
            val cursor: Cursor = imagDatabase.rawQuery(
                "select * from imageStore",
                null
            )
            if (cursor.getCount() !== 0) {
                while (cursor.moveToNext()) {
                    val imageName: String = cursor.getString(0)
                    val image: ByteArray = cursor.getBlob(1)
                    val imageBitmap = BitmapFactory.decodeByteArray(
                        image,
                        0, image.size
                    )
                    dbImages.add(ImageModel(imageName, imageBitmap, null))
                }
                Toast.makeText(
                    context, "Loading Images",
                    Toast.LENGTH_SHORT
                ).show()
                dbImages
            } else {
                Toast.makeText(
                    context, "No Images Found ",
                    Toast.LENGTH_SHORT
                ).show()
                null
            }
        } catch (e: java.lang.Exception) {
            Log.i("SAVE TO DB ", "storeImageLocal: " + e.message)
            null
        }
    }
}