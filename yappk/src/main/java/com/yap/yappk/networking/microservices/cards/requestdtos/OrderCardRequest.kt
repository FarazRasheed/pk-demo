package com.yap.yappk.networking.microservices.cards.requestdtos

import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.microservices.cards.responsedtos.CardScheme

data class OrderCardRequest(
    @SerializedName("cardFee")
    val cardFee: String? = null,
    @SerializedName("cardSchemeTitle")
    val cardScheme: String? = null,
)
