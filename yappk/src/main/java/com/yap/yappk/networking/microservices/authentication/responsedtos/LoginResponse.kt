package com.yap.yappk.networking.microservices.authentication.responsedtos

import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("id_token")
    val idToken: String? = null
) : BaseApiResponse()