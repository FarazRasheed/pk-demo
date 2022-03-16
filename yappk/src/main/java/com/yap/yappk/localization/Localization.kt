package com.yap.yappk.localization

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.PluralsRes
import androidx.fragment.app.Fragment

fun Context.getString(keyID: Int, vararg args: String) = getString(keyID, *args)
fun Activity.getString(keyID: Int, vararg args: String) = getString(keyID, *args)
fun Fragment.getString(keyID: Int, vararg args: String) = getString(keyID, *args)
fun View.getString(stringResId: Int): String = resources.getString(stringResId)

fun View.getString(keyID: String) = context.getString(keyID)

fun View.getString(keyID: String, vararg args: String): String {
    val stringResourceId = resources.getIdentifier(keyID, "string", context.packageName)
    return context.getString(stringResourceId, *args)
}

fun Context.getString(keyID: String, vararg args: String): String {
    val stringResourceId = resources.getIdentifier(keyID, "string", packageName)
    return getString(stringResourceId, *args)
}

fun Context.getString(keyID: String): String {
    val stringResourceId = resources.getIdentifier(keyID, "string", packageName)
    return getString(stringResourceId)
}

fun Context.getFormattedQuantityString(
    @PluralsRes id: Int,
    quantity: Int,
    formatValue: String = ""
): String {
    return if (formatValue.isEmpty()) {
        this.resources.getQuantityString(id, quantity)
    } else {
        this.resources.getQuantityString(id, quantity, formatValue)
    }
}

fun Context.getQuantityString(@PluralsRes id: Int, quantity: Int): String {
    return this.resources.getQuantityString(id, quantity)
}