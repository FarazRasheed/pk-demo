package com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Bank(
    @SerializedName("creationDate")
    var creationDate: String?=null,
    @SerializedName("createdBy")
    var createdBy: String?=null,
    @SerializedName("id")
    var id: Int?=0,
    @SerializedName("swiftCode")
    var swiftCode: String?=null,
    @SerializedName("bankCode")
    var bankCode: String?=null,
    @SerializedName("name")
    var name: String?=null,
    @SerializedName("address")
    var address: String?=null,
    @SerializedName("isActive")
    var isActive: String?=null
):Parcelable
