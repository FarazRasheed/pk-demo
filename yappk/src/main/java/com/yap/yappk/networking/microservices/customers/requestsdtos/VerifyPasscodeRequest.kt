package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName

data class VerifyPasscodeRequest(
    @SerializedName("passcode")
    val passcode: String? = null
)