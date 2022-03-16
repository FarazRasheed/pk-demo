package com.yap.yappk.networking.microservices.customers.requestsdtos


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardOrderRequest(
    @SerializedName("address1")
    val address1: String? = null,
    @SerializedName("address2")
    val address2: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("postCode")
    val postCode: String? = null
) : Parcelable