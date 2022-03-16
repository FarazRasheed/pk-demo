package com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.microservices.transactions.responsedtos.OrderResponse
import com.yap.yappk.networking.microservices.transactions.responsedtos.Session
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateSession(
    @SerializedName("order") val order: OrderResponse?,
    @SerializedName("session") val session: Session?
) : Parcelable