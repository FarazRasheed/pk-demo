package com.yap.yappk.networking.microservices.cards.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardLimitConfigRequest(
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String
) : Parcelable

