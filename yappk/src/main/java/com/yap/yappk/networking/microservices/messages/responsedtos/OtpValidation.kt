package com.yap.yappk.networking.microservices.messages.responsedtos

import com.google.gson.annotations.SerializedName

data class OtpValidation(
    @SerializedName("otpToken") var token: String? = "",
    @SerializedName("waitingListRank") var rankNo: String? = "",
    @SerializedName("isWaiting") var isWaiting: Boolean? = false
)