package com.yap.yappk.networking.microservices.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardSchemeBenefit(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("scheme")
    val scheme: String? = null,
    @SerializedName("isActive")
    val isActive: Boolean? = null,
    @SerializedName("description")
    val description: String? = null,
) : Parcelable
