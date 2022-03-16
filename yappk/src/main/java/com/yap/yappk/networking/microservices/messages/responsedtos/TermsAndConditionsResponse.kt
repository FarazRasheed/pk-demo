package com.yap.yappk.networking.microservices.messages.responsedtos

import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse

data class TermsAndConditionsResponse(
    @SerializedName("data")
    var data: String? = ""
) : BaseApiResponse()