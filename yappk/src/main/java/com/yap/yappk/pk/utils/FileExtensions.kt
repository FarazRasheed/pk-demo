package com.yap.yappk.pk.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.FileProvider
import com.digitify.core.utils.DateUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

fun Context.shareImage(
    rootView: View,
    imageName: String,
    shareText: String? = null,
    chooserTitle: String,
    applicationId: String
) {
    val bitmap: Bitmap = takeScreenshotForView(rootView)
    var bmpUri: Uri? = null
    val fileName = "${imageName}.jpg"
    val file = createTempFile(fileName)
    try {
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    bmpUri = FileProvider.getUriForFile(
        this,
        "$applicationId.provider", file
    )

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    if (!shareText.isNullOrBlank()) {
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            shareText
        )
    }
    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
    shareIntent.type = "image/jpeg"
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (shareIntent.resolveActivity(packageManager) != null)
        startActivity(Intent.createChooser(shareIntent, chooserTitle))
}

fun takeScreenshotForView(view: View): Bitmap {
    view.measure(
        View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
    )
    view.layout(
        view.x.toInt(),
        view.y.toInt(),
        view.x.toInt() + view.measuredWidth,
        view.y.toInt() + view.measuredHeight
    )
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun Context.createTempFile(extension: String): File {
    val dir = File(this.filesDir, "yapTemp")
    if (!dir.exists()) {
        dir.mkdirs()
        dir.mkdir()
    }
    val time = System.currentTimeMillis().toString()
    return File(dir, "${time}.$extension")
}

// this method saves the image(bitmap) to gallery
fun Context.storeBitmap(rootView: View, success: () -> Unit) {
    val bitmap: Bitmap = takeScreenshotForView(rootView)
    val randomNum = (0..10).random()
    val imageDate = "$randomNum " + getCurrentDateTime()
    val imageName = "YAP-${imageDate}-qrCode.jpg"
    // Output stream
    var fos: OutputStream? = null

    // For devices running android >= Q
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // getting the contentResolver
        this.contentResolver?.also { resolver ->

            // Content resolver will process the contentvalues
            val contentValues = ContentValues().apply {

                // putting file information in content values
                put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            // Inserting the contentValues to
            // contentResolver and getting the Uri
            val imageUri: Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            // Opening an outputstream with the Uri that we got
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        // These for devices running on android < Q
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, imageName)
        fos = FileOutputStream(image)
    }

    fos?.use {
        // Finally writing the bitmap to the output stream that we opened
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        success.invoke()
    }
}

fun getCurrentDateTime(): String { // need to re verify
    val currentCalendar: Calendar = Calendar.getInstance()
    return DateUtils.dateToString(currentCalendar.time, "dd-mm-yyyy", DateUtils.TIME_ZONE_Default)
}

@Throws(IOException::class)
fun Activity.createImageFile(): File {
    // Create an image file name
    val timeStamp: String = getCurrentDateTime()
    val storageDir: File? =
        this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )
}