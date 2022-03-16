package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterAmount(
    @SerializedName("minAmount")
    val minAmount: String? = null,
    @SerializedName("maxAmount")
    val maxAmount: String? = null
) : Parcelable
