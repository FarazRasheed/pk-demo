package com.yap.yappk.networking.microservices.authentication.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginRequest(
    @SerializedName("client_id")
    var clientId: String? = null,
    @SerializedName("client_secret")
    var clientSecret: String? = null,
    @SerializedName("device_id")
    var deviceId: String? = null,
    @SerializedName("grant_type")
    var grantType: String? = "client_credentials"
) : BaseApiResponse(), Parcelable