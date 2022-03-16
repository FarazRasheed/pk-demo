package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExternalCard(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("logo")
    val logo: String? = "",
    @SerializedName("expiry")
    val expiry: String? = "",
    @SerializedName("number")
    var number: String? = "",
    @SerializedName("alias")
    val alias: String? = "",
    @SerializedName("color")
    val color: String? = "",
    @Transient
    var isTopUp: Boolean = false,
    @Transient
    var topUpAmount: String? = null,
    @Transient
    var sessionId: String? = null
) : Parcelable
