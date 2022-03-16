package com.yap.yappk.networking.microservices.cards.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardsHotListRequest(
    @SerializedName("cardSerialNumber")
    val cardSerialNumber: String? = null,
    @SerializedName("hotListReason")
    val hotListReason: String? = null,
    @SerializedName("waiveFee")
    val waiveFee: Boolean? = null,
    @SerializedName("changePermanentAddress")
    val changePermanentAddress: Boolean? = null,
    @SerializedName("blocked")
    val blocked: Boolean? = null,
) : Parcelable
