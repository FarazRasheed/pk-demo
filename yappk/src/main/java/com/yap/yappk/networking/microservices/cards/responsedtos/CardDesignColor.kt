package com.yap.yappk.networking.microservices.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDesignColor(
    @SerializedName("colorCode")
    val colorCode: String? = null,
    @SerializedName("designCodeUUID")
    val designCodeUUID: String? = null
):Parcelable
