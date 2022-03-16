package com.yap.yappk.pk.di

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.digitify.core.extensions.getColors
import com.digitify.core.utils.getText
import com.yap.yappk.localization.getFormattedQuantityString
import com.yap.yappk.localization.getString
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProviders @Inject constructor(@ApplicationContext val context: Context) {
    fun getColor(@ColorRes id: Int) = context.getColors(id)

    fun getDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(context, id)

    fun getString(@StringRes resId: Int, vararg args: String) = context.getString(resId, args)
    fun getString(keyID: String, vararg args: String) =
        context.getString(keyID = keyID, args = args)

    fun getSpannableText(id: String, vararg formatArgs: Any?): CharSequence =
        context.resources.getText(id = id, formatArgs = formatArgs)

    fun getString(keyID: String) = context.getString(keyID = keyID)
    fun getString(@StringRes resId: Int) = context.getString(resId)
    fun getFormattedQuantityString(@PluralsRes id: Int, quantity: Int, formatValue: String) =
        context.getFormattedQuantityString(id, quantity, formatValue)
}
