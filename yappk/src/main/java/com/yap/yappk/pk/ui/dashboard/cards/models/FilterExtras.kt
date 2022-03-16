package com.yap.yappk.pk.ui.dashboard.cards.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterExtras(
    var txnType: String? = null,
    var minRange: String? = null,
    var maxRange: String? = null,
    var isFilterSet: Boolean = false,
    var filterCount: Int = 0
) : Parcelable
