package com.yap.yappk.localization

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleManager {
    fun setLocale(c: Context): Context {
        return setNewLocale(c, getLanguage(c))
    }

    private fun setNewLocale(c: Context, locale: Locale): Context {
        return updateResources(c, locale)
    }

    private fun getLanguage(c: Context): Locale {
        return Locale("en", "US")
    }

    private fun updateResources(context: Context, locale: Locale): Context {
        var wrapperContext: Context = context
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            wrapperContext = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return wrapperContext
    }
}