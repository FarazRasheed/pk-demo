package com.yap.yappk.networking.microservices.transactions.requestdtos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.microservices.transactions.responsedtos.Session
import kotlinx.parcelize.Parcelize

@Parcelize
data class Check3DEnrollmentSessionRequest(
    @SerializedName("beneficiaryId")
    val beneficiaryId: Int? = null,
    @SerializedName("order")
    val order: Order? = null,
    @SerializedName("session")
    val session: Session? = null,
) : Parcelable