package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    @SerializedName("active")
    val active: Boolean? = null,
    @SerializedName("cityCode")
    val cityCode: String? = null,
    @SerializedName("iata3Code")
    val iata3Code: String? = null,
    @SerializedName("name")
    val name: String? = null
) : Parcelable
