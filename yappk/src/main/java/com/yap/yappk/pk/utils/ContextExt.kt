package com.yap.yappk.pk.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.google.zxing.BarcodeFormat


fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("YapAccount", text)
    clipboard.setPrimaryClip(clip)
}

fun Context.generateQrCode(resourceKey: String): Drawable? {
    return try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap? =
            barcodeEncoder.encodeBitmap(resourceKey, BarcodeFormat.QR_CODE, 400, 400)
        BitmapDrawable(resources, bitmap)
    } catch (e: Exception) {
        null
    }
}