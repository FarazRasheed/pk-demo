package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    @SerializedName("amount")
    var amount: String? = null,
    @SerializedName("creationDateTime")
    var creationDateTime: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("currency")
    var currency: String? = null,
    @SerializedName("type")
    var transactionType: String? = null,
    @SerializedName("category")
    var transactionCategory: String? = null,
    @SerializedName("productCode")
    var productCode: String? = null,
    @Transient
    var totalAmount: String = "",
    @Transient
    var type: Int = 0
) : Parcelable
