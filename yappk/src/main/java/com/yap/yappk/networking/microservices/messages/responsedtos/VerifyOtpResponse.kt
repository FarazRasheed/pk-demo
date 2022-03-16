package com.yap.yappk.networking.microservices.messages.responsedtos

import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse

class VerifyOtpResponse : BaseApiResponse() {
    @SerializedName("data")
    var data: String? = ""
}