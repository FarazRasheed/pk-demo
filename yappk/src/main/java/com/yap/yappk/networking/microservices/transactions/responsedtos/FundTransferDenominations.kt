package com.yap.yappk.networking.microservices.transactions.responsedtos

import com.google.gson.annotations.SerializedName

class FundTransferDenominations(
    @SerializedName("amount") val amount: String,
    @SerializedName("active") val active: Boolean
)