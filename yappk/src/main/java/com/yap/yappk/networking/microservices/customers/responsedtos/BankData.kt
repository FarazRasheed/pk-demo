package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class BankData(
    @SerializedName("bankLogoUrl") val bankLogoUrl: String? = "",
    @SerializedName("bankName") val bankName: String? = "",
    @SerializedName("accountNoMinLength") val accountNoMinLength: Int? = 0,
    @SerializedName("accountNoMaxLength") val accountNoMaxLength: Int? = 0,
    @SerializedName("ibanMinLength") val ibanMinLength: Int? = 0,
    @SerializedName("ibanMaxLength") val ibanMaxLength: Int? = 0,
    @SerializedName("consumerId") val consumerId: String? = "",
    @SerializedName("formatMessage") val formatMessage: String? = "",
    @Transient
    var accountHolderTitle: String? = "",
    @Transient
    var accountNumber: String? = "",
    @Transient
    var ibanNumber: String? = "",
) : Parcelable
