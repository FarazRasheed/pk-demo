package com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession

import com.google.gson.annotations.SerializedName

class Check3DEnrollmentSession(
    @SerializedName("3DSecureId") val _3DSecureId: String?,
    @SerializedName("3DSecure") val _3DSecure: Enrollment3DSecure
)