package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FundTransferLimits(
    @SerializedName("minLimit") val minLimit: String,
    @SerializedName("maxLimit") val maxLimit: String,
    @SerializedName("active") val active: Boolean
): Parcelable