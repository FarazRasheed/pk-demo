package com.yap.yappk.pk.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


class BarcodeEncoder {
    private val YAP_DARK = -0xd8dd9e
    private val YAP_LIGHT = -0xa1ca4f

    @Throws(WriterException::class)
    fun encodeBitmap(contents: String?, format: BarcodeFormat?, width: Int, height: Int): Bitmap? {
        return encode(contents, format, width, height)?.let { createBitmap(it) }
    }

    @Throws(WriterException::class)
    fun encodeBitmap(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int,
        hints: Map<EncodeHintType?, *>?
    ): Bitmap? {
        return encode(contents, format, width, height, hints)?.let { createBitmap(it) }
    }

    @Throws(WriterException::class)
    fun encode(contents: String?, format: BarcodeFormat?, width: Int, height: Int): BitMatrix? {
        return try {
            MultiFormatWriter().encode(contents, format, width, height)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            // ZXing sometimes throws an IllegalArgumentException
            throw WriterException(e)
        }
    }

    @Throws(WriterException::class)
    fun encode(
        contents: String?,
        format: BarcodeFormat?,
        width: Int,
        height: Int,
        hints: Map<EncodeHintType?, *>?
    ): BitMatrix? {
        return try {
            MultiFormatWriter().encode(contents, format, width, height, hints)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            throw WriterException(e)
        }
    }

    private fun createBitmap(matrix: BitMatrix): Bitmap? {
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
//                    pixels[offset + x] = matrix.get(x, y) ? YAP_DARK : WHITE;
                if (matrix[x, y]) {
                    if (y == x && y == x / 2) {
                        pixels[offset + x] = YAP_LIGHT
                    } else pixels[offset + x] = YAP_DARK
                }
                //                    if (matrix.getEnclosingRectangle() != null){
//                       int [] rect =  matrix.getEnclosingRectangle();
//                    }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

}