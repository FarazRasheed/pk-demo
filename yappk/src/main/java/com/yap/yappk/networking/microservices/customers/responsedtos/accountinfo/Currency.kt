package com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    @SerializedName("code")
    var code: String? = "",
    @SerializedName("default")
    var default: Boolean? = false,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("cashPickUp")
    var cashPickUp: Boolean? = false,
    @SerializedName("rmtCountry")
    var rmtCountry: Boolean? = false,
    @SerializedName("creationDate")
    var creationDate: String? = "",
    @SerializedName("createdBy")
    var createdBy: String? = "",
    @SerializedName("updatedDate")
    var updatedDate: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("symbol")
    var symbol: Boolean? = false,
    @SerializedName("isoNum")
    var isoNum: String? = ""
) : Parcelable