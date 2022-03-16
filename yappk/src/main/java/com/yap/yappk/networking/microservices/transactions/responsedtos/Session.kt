package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Session(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("number")
    val number: String? = null
) : Parcelable