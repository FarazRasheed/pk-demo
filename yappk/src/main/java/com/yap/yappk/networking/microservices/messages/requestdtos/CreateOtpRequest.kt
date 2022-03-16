package com.yap.yappk.networking.microservices.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class CreateOtpRequest(
    @SerializedName("action")
    val action: String? = null,
)
