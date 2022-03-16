package com.yap.yappk.networking.microservices.customers.requestsdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class CompleteVerificationRequest(
    @SerializedName("countryCode")
    val countryCode: String? = null,
    @SerializedName("mobileNo")
    val mobileNo: String? = null
) : Serializable, Parcelable
