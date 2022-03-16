package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName

data class AddExternalCardRequest(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("session")
    val session: CardSession
)