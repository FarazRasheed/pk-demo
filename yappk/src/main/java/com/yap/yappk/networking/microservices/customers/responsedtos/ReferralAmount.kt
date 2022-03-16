package com.yap.yappk.networking.microservices.customers.responsedtos

import com.google.gson.annotations.SerializedName

data class ReferralAmount(
    @SerializedName("referralAmount")
    var referralAmount: String? = null,
    @SerializedName("currencyCode")
    var currencyCode: String? = null,
)
