package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmailVerificationRequest(
    @SerializedName("email") val email: String,
    @SerializedName("accountType") val accountType: String,
    @SerializedName("token") val token: String? = null
) : Serializable
