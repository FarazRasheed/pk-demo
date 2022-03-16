package com.yap.yappk.networking.microservices.transactions.requestdtos

import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.microservices.transactions.responsedtos.Session

data class CreateSessionRequest(
    @SerializedName("order")
    var order: Order?,
    @SerializedName("session")
    var session: Session? = null
)