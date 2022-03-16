package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FindBankAccountInfoResponse(
    @SerializedName("accountNo")
    val accountNo: String? = null,
    @SerializedName("iban")
    val iban: String? = null,
    @SerializedName("title")
    val title: String? = null
) : Parcelable
