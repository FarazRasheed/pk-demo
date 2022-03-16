package com.digitify.testyappakistan.onboarding.countryCodePicker

import android.graphics.drawable.Drawable

data class CountryItem(
    var id: Int? = null,
    var title: CharSequence? = null,
    var code: CharSequence? = null,
    var icon: Drawable? = null,
    var iconStart: Drawable? = null
)
