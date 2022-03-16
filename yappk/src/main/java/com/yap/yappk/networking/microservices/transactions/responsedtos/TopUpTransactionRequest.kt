package com.yap.yappk.networking.microservices.transactions.responsedtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.microservices.transactions.requestdtos.Order
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopUpTransactionRequest(
    @SerializedName("3DSecureId")
    val `3DSecureId`: String? = null,
    @SerializedName("beneficiaryId")
    val beneficiaryId: String? = null,
    @SerializedName("order")
    val order: Order? = null,
    @SerializedName("securityCode")
    val securityCode: String? = null,
    @SerializedName("session")
    val session: Session? = null
) : Parcelable