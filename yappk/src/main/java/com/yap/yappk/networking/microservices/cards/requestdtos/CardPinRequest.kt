package com.yap.yappk.networking.microservices.cards.requestdtos

import com.google.gson.annotations.SerializedName

data class CardPinRequest(
    @SerializedName("newPin")
    val cardPin: String? = null
)
