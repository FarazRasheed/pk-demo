package com.yap.yappk.networking.microservices.cards.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.uikit.widget.multiStateView.StateEnum
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardDetail(
    @SerializedName("cvv2")
    val cvv2: String? = null,
    @SerializedName("cardToken")
    val cardToken: String? = null,
    @SerializedName("expiry")
    val expiry: String? = null,
    @SerializedName("cVV2")
    val cVV2: String? = null,
    @SerializedName("responseCode")
    val responseCode: String? = null,
    @Transient
    var state: StateEnum = StateEnum.LOADING,
    @Transient
    var title: String = "",
    @Transient
    var cardType: String = ""
) : Parcelable
