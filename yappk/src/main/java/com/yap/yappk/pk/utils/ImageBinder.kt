package com.yap.yappk.pk.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

object ImageBinder {
    @JvmStatic
    @BindingAdapter(value = ["loadImg", "errorImg", "placeHolder"], requireAll = false)
    fun loadImageSrc(
        imageView: ImageView,
        resource: String?,
        errorRecourse: Drawable? = null,
        placeRecourse: Drawable? = null
    ) {
        errorRecourse?.let { err ->
            Glide.with(imageView).load(resource).placeholder(placeRecourse).error(err)
                .into(imageView)
        } ?: Glide.with(imageView).load(resource).into(imageView)
    }

    fun ImageView.loadImage(
        resource: String?,
        errorRecourse: Drawable? = null,
        placeRecourse: Drawable? = null
    ) {
        errorRecourse?.let { err ->
            Glide.with(this).load(resource).placeholder(placeRecourse).error(err)
                .into(this)
        } ?: Glide.with(this).load(resource).into(this)

    }

    fun ImageView.loadImage(
        resource: File?,
        errorRecourse: Drawable? = null,
        placeRecourse: Drawable? = null
    ) {
        errorRecourse?.let { err ->
            Glide.with(this).load(resource).placeholder(placeRecourse).error(err)
                .into(this)
        } ?: Glide.with(this).load(resource).into(this)

    }

    fun ImageView.loadImage(
        resource: Drawable?,
        errorRecourse: Drawable? = null,
        placeRecourse: Drawable? = null
    ) {
        errorRecourse?.let { err ->
            Glide.with(this).load(resource).placeholder(placeRecourse).error(err)
                .into(this)
        } ?: Glide.with(this).load(resource).into(this)

    }
    
}
