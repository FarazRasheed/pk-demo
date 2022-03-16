package com.yap.yappk.networking.microservices.customers.requestsdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class CardNameRequest(
    @SerializedName("cardName")
    val cardName: String? = null
) : Serializable, Parcelable
