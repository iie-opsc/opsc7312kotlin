package za.ac.iie.opsc.photomemories.model

import android.graphics.Bitmap

data class ImageModel (public val imageName: String?,
                       public val imageBitmap: Bitmap?,
                       public val imageUri: String?) {

    public constructor() : this(null, null, null) {
    }
}