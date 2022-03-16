package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionFeeResponse(
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("amount")
    val amount: Double? = null,
    @SerializedName("feeCurrency")
    val feeCurrency: String? = null,
    @SerializedName("fixedAmount")
    val fixedAmount: Double? = null
) : Parcelable
