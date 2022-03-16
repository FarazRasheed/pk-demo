package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FundTransferReasons(
    @SerializedName("transferReason") val transferReason: String? = null,
    @SerializedName("code") val code: String? = null
):Parcelable
