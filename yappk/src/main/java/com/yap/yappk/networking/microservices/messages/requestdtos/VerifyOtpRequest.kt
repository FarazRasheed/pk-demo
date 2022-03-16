package com.yap.yappk.networking.microservices.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(
    @SerializedName("action")
    val action: String? = null,
    @SerializedName("otp")
    val otp: String? = null,
)
