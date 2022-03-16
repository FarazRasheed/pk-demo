package com.yap.yappk.networking.microservices.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardBalance(
    @SerializedName("currencyCode")
    val currencyCode: String? = null,
    @SerializedName("availableBalance")
    val availableBalance: String? = null,
) : Parcelable
