package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountBalance(
    @SerializedName("currentBalance")
    val currentBalance: Double? = null,
    @SerializedName("currency")
    val currency: String? = null,
) : Parcelable
