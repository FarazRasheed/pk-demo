package com.yap.yappk.networking.microservices.cards.requestdtos

import com.google.gson.annotations.SerializedName

data class ForgotCardPINRequest(
    @SerializedName("newPin")
    val newPin: String? = null,
    @SerializedName("token")
    val token: String? = null
)
