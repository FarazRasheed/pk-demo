package com.yap.yappk.networking.microservices.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardScheme(
    @SerializedName("schemeName")
    val schemeName: String? = null,
    @SerializedName("schemeCode")
    val schemeCode: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = null,
    @SerializedName("fee")
    val fee: Double? = null,
) : Parcelable
