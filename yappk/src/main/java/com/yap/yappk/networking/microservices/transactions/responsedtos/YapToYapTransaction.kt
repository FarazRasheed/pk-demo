package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class YapToYapTransaction(
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("balance")
    val balance: String? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("contactNumber")
    val contactNumber: String? = null,
    @SerializedName("amountTransferred")
    val amountTransferred: String? = null,
    @SerializedName("date")
    val date: String? = null,
) : Parcelable
