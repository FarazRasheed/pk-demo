package com.yap.yappk.networking.microservices.messages.requestdtos

import com.google.gson.annotations.SerializedName

data class CreateOtpOnboardingRequest(
    @SerializedName("countryCode") val countryCode: String? = null,
    @SerializedName("mobileNo") val mobileNo: String? = null,
    @SerializedName("accountType") val accountType: String? = null//only this line was already serialized
)
