package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankTransferResponse(
    @SerializedName("transactionId")
    val transactionId: String? = null,
    @SerializedName("currency")
    val currency: String? = null,
    @SerializedName("accountNo")
    val accountNo: String? = null,
    @SerializedName("iban")
    val iban: String? = null,
    @SerializedName("bankLogoURL")
    val bankLogoURL: String? = null,
    @SerializedName("bankName")
    val bankName: String? = null,
    @SerializedName("amountTransferred")
    val amountTransferred: String? = null,
    @SerializedName("date")
    val date: String? = null
) : Parcelable