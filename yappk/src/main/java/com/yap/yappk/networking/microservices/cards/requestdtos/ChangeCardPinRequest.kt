package com.yap.yappk.networking.microservices.cards.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeCardPinRequest(
    @SerializedName("oldPin")
    var oldPin: String,
    @SerializedName("newPin")
    var newPin: String,
    @SerializedName("confirmPin")
    var confirmPin: String,
    @SerializedName("cardSerialNumber")
    var cardSerialNumber: String
) : Parcelable
