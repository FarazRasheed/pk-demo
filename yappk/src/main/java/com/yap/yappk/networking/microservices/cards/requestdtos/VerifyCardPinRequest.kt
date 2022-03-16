package com.yap.yappk.networking.microservices.cards.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VerifyCardPinRequest (
    @SerializedName("pin")
    val cardPin: String? = null
) : Parcelable
