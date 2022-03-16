package com.yap.yappk.networking.microservices.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class ForgotPasscodeOtpRequest(
    @SerializedName("destination")
    val destination: String? = null,
    @SerializedName("emailOTP")
    val emailOTP: Boolean? = null,
    @SerializedName("otp")
    val otp: String? = null,
)