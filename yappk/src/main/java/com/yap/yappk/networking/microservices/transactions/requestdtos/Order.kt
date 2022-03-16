package com.yap.yappk.networking.microservices.transactions.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Order(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("amount")
    var amount: String? = ""
) : Parcelable