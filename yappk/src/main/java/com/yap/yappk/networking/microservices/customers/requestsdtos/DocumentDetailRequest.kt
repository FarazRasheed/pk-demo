package com.yap.yappk.networking.microservices.customers.requestsdtos

import com.google.gson.annotations.SerializedName

data class DocumentDetailRequest(
    @SerializedName("cnic")
    val cnic: String? = null,
    @SerializedName("dateOfIssuance")
    val dateOfIssuance: String? = null
)
