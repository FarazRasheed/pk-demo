package com.yap.yappk.networking.microservices.customers.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactAccountDetail(
    @SerializedName("accountNo")
    val accountNo: String? = "",
    @SerializedName("accountType")
    val accountType: String? = "",
    @SerializedName("accountUuid")
    val accountUuid: String? = ""
) : Parcelable