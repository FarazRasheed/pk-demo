package com.yap.yappk.networking.microservices.customers.requestsdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardSession(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("number")
    val number: String? = null
) : Parcelable